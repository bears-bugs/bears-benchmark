package org.corfudb.runtime;

import com.codahale.metrics.MetricRegistry;
import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import java.lang.Thread.UncaughtExceptionHandler;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.corfudb.comm.ChannelImplementation;
import org.corfudb.protocols.wireprotocol.VersionInfo;
import org.corfudb.recovery.FastObjectLoader;
import org.corfudb.runtime.clients.BaseClient;
import org.corfudb.runtime.clients.IClientRouter;
import org.corfudb.runtime.clients.LayoutClient;
import org.corfudb.runtime.clients.LogUnitClient;
import org.corfudb.runtime.clients.ManagementClient;
import org.corfudb.runtime.clients.NettyClientRouter;
import org.corfudb.runtime.clients.SequencerClient;
import org.corfudb.runtime.exceptions.NetworkException;
import org.corfudb.runtime.exceptions.ShutdownException;
import org.corfudb.runtime.exceptions.WrongClusterException;
import org.corfudb.runtime.exceptions.unrecoverable.UnrecoverableCorfuError;
import org.corfudb.runtime.exceptions.unrecoverable.UnrecoverableCorfuInterruptedError;
import org.corfudb.runtime.view.AddressSpaceView;
import org.corfudb.runtime.view.Layout;
import org.corfudb.runtime.view.LayoutManagementView;
import org.corfudb.runtime.view.LayoutView;
import org.corfudb.runtime.view.ManagementView;
import org.corfudb.runtime.view.ObjectsView;
import org.corfudb.runtime.view.SequencerView;
import org.corfudb.runtime.view.StreamsView;
import org.corfudb.util.CFUtils;
import org.corfudb.util.GitRepositoryState;
import org.corfudb.util.MetricsUtils;
import org.corfudb.util.NodeLocator;
import org.corfudb.util.Sleep;
import org.corfudb.util.UuidUtils;
import org.corfudb.util.Version;

/**
 * Created by mwei on 12/9/15.
 */
@Slf4j
@Accessors(chain = true)
public class CorfuRuntime {

    /** A class which holds parameters and settings for the {@link CorfuRuntime}.
     *
     */
    @Builder
    @Data
    public static class CorfuRuntimeParameters {

        // region Object Layer Parameters
        /** True, if undo logging is disabled. */
        @Default boolean undoDisabled = false;

        /** True, if optimistic undo logging is disabled. */
        @Default boolean optimisticUndoDisabled = false;

        /**
         * Use fast loader to restore objects on connection.
         *
         * <p>If using this utility, you need to be sure that no one
         * is accessing objects until the tables are loaded
         * (i.e. when connect returns)
         */
        @Default boolean useFastLoader = false;

        /** Set the bulk read size. */
        @Default int bulkReadSize = 10;

        /**
         * How much time the Fast Loader has to get the maps up to date.
         *
         * <p>Once the timeout is reached, the Fast Loader gives up. Every map that is
         * not up to date will be loaded through normal path.
         *
         */
        @Default Duration fastLoaderTimeout = Duration.ofMinutes(30);
        // endregion

        // region Address Space Parameters
        /** Number of times to attempt to read before hole filling. */
        @Default int holeFillRetry = 10;

        /** Whether or not to disable the cache. */
        @Default boolean cacheDisabled = false;

        /** The maximum size of the cache, in bytes. */
        @Default long numCacheEntries = 5000;

        /** Sets expireAfterAccess and expireAfterWrite in seconds. */
        @Default long cacheExpiryTime = Long.MAX_VALUE;
        // endregion

        // region Handshake Parameters
        /** Sets handshake timeout in seconds. */
        @Default int handshakeTimeout = 10;
        // endregion

        // region Stream Parameters
        /** Whether or not to disable backpointers. */
        @Default boolean backpointersDisabled = false;

        /** Whether or not hole filling should be disabled. */
        @Default boolean holeFillingDisabled = false;

        /** Number of times to retry on an
         * {@link org.corfudb.runtime.exceptions.OverwriteException} before giving up. */
        @Default int writeRetry = 5;

        /** The number of times to retry on a retriable
         * {@link org.corfudb.runtime.exceptions.TrimmedException} during a transaction.*/
        @Default int trimRetry = 2;
        // endregion

        //region        Security parameters
        /** True, if TLS is enabled. */
        @Default boolean tlsEnabled = false;

        /** A path to the key store. */
        String keyStore;

        /** A file containing the password for the key store. */
        String ksPasswordFile;

        /** A path to the trust store. */
        String trustStore;

        /** A path containing the password for the trust store. */
        String tsPasswordFile;

        /** True, if SASL plain text authentication is enabled. */
        @Default boolean saslPlainTextEnabled = false;

        /** A path containing the username file for SASL. */
        String usernameFile;

        /** A path containing the password file for SASL. */
        String passwordFile;
        //endregion

        //region Connection parameters
        /**
         * {@link Duration} before requests timeout.
         */
        @Default Duration requestTimeout = Duration.ofSeconds(5);

        /**
         * {@link Duration} before connections timeout.
         */
        @Default Duration connectionTimeout = Duration.ofMillis(500);

        /**
         * {@link Duration} before reconnecting to a disconnected node.
         */
        @Default Duration connectionRetryRate = Duration.ofSeconds(1);

        /**
         * The {@link UUID} for this client. Randomly generated by default.
         */
        @Default UUID clientId = UUID.randomUUID();

        /** The {@link UUID} for the cluster this client is connecting to, or
         * {@code null} if the client should adopt the {@link UUID} of the first
         *  server it connects to.
         */
        @Default UUID clusterId = null;

        /** The type of socket which {@link NettyClientRouter}s should use. By default,
         *  an NIO based implementation is used.
         */
        @Default
        ChannelImplementation socketType = ChannelImplementation.NIO;

        /** The initial list of layout servers. */
        @Singular List<NodeLocator> layoutServers;
        //endregion

        //region Threading Parameters
        /** The {@link EventLoopGroup} which {@link NettyClientRouter}s will use.
         *  If not specified, the runtime will generate this using the {@link ChannelImplementation}
         *  specified in {@code socketType} and the {@link ThreadFactory} specified in
         *  {@code nettyThreadFactory}.
         */
        EventLoopGroup nettyEventLoop;

        /** A string which will be used to set the
         * {@link com.google.common.util.concurrent.ThreadFactoryBuilder#nameFormat} for the
         * {@code nettyThreadFactory}. By default, this is set to "netty-%d".
         * If you provide your own {@code nettyEventLoop}, this field is ignored.
         */
        @Default String nettyEventLoopThreadFormat = "netty-%d";

        /** The number of threads that should be available in the {@link NettyClientRouter}'s
         *  event pool. 0 means that we will use 2x the number of processors reported in the
         *  system. If you provide your own {@code nettyEventLoop}, this field is ignored.
         */
        @Default int nettyEventLoopThreads = 0;

        /** True, if the {@code NettyEventLoop} should be shutdown when the runtime is
         *  shutdown. False otherwise.
         */
        @Default boolean shutdownNettyEventLoop = true;

        /** Netty channel options, if provided. If no options are set, we default to
         *  the defaults in {@link this#DEFAULT_CHANNEL_OPTIONS}.
         */
        @Singular Map<ChannelOption, Object> customNettyChannelOptions;

        /** Default channel options, used if there are no options in the
         * {@link this#customNettyChannelOptions} field.
         */
        static final Map<ChannelOption, Object> DEFAULT_CHANNEL_OPTIONS =
                ImmutableMap.<ChannelOption, Object>builder()
                    .put(ChannelOption.TCP_NODELAY, true)
                    .put(ChannelOption.SO_KEEPALIVE, true)
                    .put(ChannelOption.SO_REUSEADDR, true)
                .build();

        /** Get the netty channel options to be used by the netty client implementation.
         *
         * @return  A map containing options which should be applied to each netty channel.
         */
        public Map<ChannelOption, Object> getNettyChannelOptions() {
            return customNettyChannelOptions.size() == 0
                ? DEFAULT_CHANNEL_OPTIONS : customNettyChannelOptions;
        }

        /** A {@link UncaughtExceptionHandler} which handles threads that have an uncaught
         *  exception. Used on all {@link ThreadFactory}s the runtime creates, but if you
         *  generate your own thread factory, this field is ignored. If this field is not set,
         *  the runtime's default handler runs, which logs an error level message.
         */
        UncaughtExceptionHandler uncaughtExceptionHandler;
        //endregion

        /**
         * The number of times to retry invalidate when a layout change is expected.
         */
        @Default int invalidateRetry = 3;
    }

    /**
     * The parameters used to configure this {@link CorfuRuntime}.
     */
    @Getter
    private final CorfuRuntimeParameters parameters;

    /**
     * The {@link EventLoopGroup} provided to netty routers.
     */
    @Getter
    private final EventLoopGroup nettyEventLoop;

    /**
     * A view of the layout service in the Corfu server instance.
     */
    @Getter(lazy = true)
    private final LayoutView layoutView = new LayoutView(this);
    /**
     * A view of the sequencer server in the Corfu server instance.
     */
    @Getter(lazy = true)
    private final SequencerView sequencerView = new SequencerView(this);
    /**
     * A view of the address space in the Corfu server instance.
     */
    @Getter(lazy = true)
    private final AddressSpaceView addressSpaceView = new AddressSpaceView(this);
    /**
     * A view of streamsView in the Corfu server instance.
     */
    @Getter(lazy = true)
    private final StreamsView streamsView = new StreamsView(this);

    /**
     * Views of objects in the Corfu server instance.
     */
    @Getter(lazy = true)
    private final ObjectsView objectsView = new ObjectsView(this);
    /**
     * A view of the Layout Manager to manage reconfigurations of the Corfu Cluster.
     */
    @Getter(lazy = true)
    private final LayoutManagementView layoutManagementView = new LayoutManagementView(this);
    /**
     * A view of the Management Service.
     */
    @Getter(lazy = true)
    private final ManagementView managementView = new ManagementView(this);

    /**
     * A list of known layout servers.
     */
    private List<String> layoutServers;

    /**
     * A map of routers, representing nodes.
     */
    public Map<String, IClientRouter> nodeRouters;

    /**
     * A completable future containing a layout, when completed.
     */
    public volatile CompletableFuture<Layout> layout;

    /** The {@link UUID} of the cluster we are currently connected to, or null, if
     *  there is no cluster yet.
     */
    @Getter
    public volatile UUID clusterId;

    /**
     * Notifies that the runtime is no longer used
     * and async retries to fetch the layout can be stopped.
     */
    @Getter
    private volatile boolean isShutdown = false;

    /**
     * Metrics: meter (counter), histogram.
     */
    private static final String mp = "corfu.runtime.";
    @Getter
    private static final String mpASV = mp + "as-view.";
    @Getter
    private static final String mpLUC = mp + "log-unit-client.";
    @Getter
    private static final String mpCR = mp + "client-router.";
    @Getter
    private static final String mpObj = mp + "object.";
    @Getter
    private static MetricRegistry defaultMetrics = new MetricRegistry();
    @Getter
    private MetricRegistry metrics = new MetricRegistry();

    public CorfuRuntime setMetrics(@NonNull MetricRegistry metrics) {
        this.metrics = metrics;
        return this;
    }

    /**
     * These two handlers are provided to give some control on what happen when system is down.
     *
     * For applications that want to have specific behaviour when a the system appears unavailable, they can
     * register their own handler for both before the rpc request and upon network exception.
     *
     * An example of how to use these handlers implementing timeout is given in
     * test/src/test/java/org/corfudb/runtime/CorfuRuntimeTest.java
     *
     */
    public Runnable beforeRpcHandler = () -> {};
    public Runnable systemDownHandler = () -> {};


    public CorfuRuntime registerSystemDownHandler(Runnable handler) {
        systemDownHandler = handler;
        return this;
    }

    public CorfuRuntime registerBeforeRpcHandler(Runnable handler) {
        beforeRpcHandler = handler;
        return this;
    }


    /**
     * When set, overrides the default getRouterFunction. Used by the testing
     * framework to ensure the default routers used are for testing.
     */
    public static BiFunction<CorfuRuntime, String, IClientRouter> overrideGetRouterFunction = null;

    /**
     * A function to handle getting routers. Used by test framework to inject
     * a test router. Can also be used to provide alternative logic for obtaining
     * a router.
     */
    @Getter
    @Setter
    public Function<String, IClientRouter> getRouterFunction = overrideGetRouterFunction != null
            ? (address) -> overrideGetRouterFunction.apply(this, address) : (address) ->
                nodeRouters.compute(address, (k, r) -> {
                    final NettyClientRouter router = (NettyClientRouter) r;
                    if (router != null && router.getConnected()) {
                        // Return an existing router if we already have one and it is connected.
                        return router;
                    } else {
                        NodeLocator node = NodeLocator.parseString(address);
                        // Generate a new router, start it and add it to the table.
                        NettyClientRouter newRouter = new NettyClientRouter(node,
                            getNettyEventLoop(),
                            getParameters());
                        log.debug("Connecting to new router {}", node);
                        try {
                            newRouter.addClient(new LayoutClient())
                                .addClient(new SequencerClient())
                                .addClient(new LogUnitClient().setMetricRegistry(metrics != null
                                    ? metrics : CorfuRuntime.getDefaultMetrics()))
                                .addClient(new ManagementClient())
                                .start();
                        } catch (Exception e) {
                            log.warn("Error connecting to router", e);
                            throw e;
                        }
                        return newRouter;
                    }
                });

    /** Factory method for generating new {@link CorfuRuntime}s given a set of
     *  {@link CorfuRuntimeParameters} to configure the runtime with.
     *
     * @param parameters    A {@link CorfuRuntimeParameters} to use.
     * @return              A new {@link CorfuRuntime}.
     */
    public static CorfuRuntime fromParameters(@Nonnull CorfuRuntimeParameters parameters) {
        return new CorfuRuntime(parameters);
    }

    /** Construct a new {@link CorfuRuntime} given a {@link CorfuRuntimeParameters} instance.
     *
     * @param parameters    {@link CorfuRuntimeParameters} to configure the runtime with.
     */
    private CorfuRuntime(@Nonnull CorfuRuntimeParameters parameters) {
        // Set the local parameters field
        this.parameters = parameters;

        // Populate the initial set of layout servers
        layoutServers = parameters.getLayoutServers().stream()
                                .map(NodeLocator::toString)
                                .collect(Collectors.toList());

        // Generate the map of routers
        nodeRouters = new ConcurrentHashMap<>();

        // Set the initial cluster Id
        clusterId = parameters.getClusterId();

        // Generate or set the NettyEventLoop
        nettyEventLoop = parameters.nettyEventLoop == null ? getNewEventLoopGroup()
                                                            : parameters.nettyEventLoop;

        synchronized (metrics) {
            if (metrics.getNames().isEmpty()) {
                MetricsUtils.metricsReportingSetup(metrics);
            }
        }
        log.info("Corfu runtime version {} initialized.", getVersionString());
    }

    /** Get a new {@link EventLoopGroup} for scheduling threads for Netty. The
     *  {@link EventLoopGroup} is typically passed to a router.
     *
     * @return  An {@link EventLoopGroup}.
     */
    private EventLoopGroup getNewEventLoopGroup() {
        // Calculate the number of threads which should be available in the thread pool.
        int numThreads = parameters.nettyEventLoopThreads == 0
                            ? Runtime.getRuntime().availableProcessors() * 2 :
                            parameters.nettyEventLoopThreads;
        ThreadFactory factory = new ThreadFactoryBuilder()
                                    .setDaemon(true)
                                    .setNameFormat(parameters.nettyEventLoopThreadFormat)
                                    .setUncaughtExceptionHandler(this::handleUncaughtThread)
                                    .build();
        return parameters.socketType.getGenerator().generate(numThreads, factory);
    }

    /** Function which is called whenever the runtime encounters an uncaught thread.
     *
     * @param thread        The thread which terminated.
     * @param throwable     The throwable which caused the thread to terminate.
     */
    private void handleUncaughtThread(@Nonnull Thread thread, @Nonnull Throwable throwable) {
        if (parameters.getUncaughtExceptionHandler() != null) {
            parameters.getUncaughtExceptionHandler().uncaughtException(thread, throwable);
        } else {
            log.error("handleUncaughtThread: {} terminated with throwable of type {}",
                    thread.getName(),
                    throwable.getClass().getSimpleName(),
                    throwable);
        }
    }

    /**
     * Shuts down the CorfuRuntime.
     * Stops async tasks from fetching the layout.
     * Cannot reuse the runtime once shutdown is called.
     */
    public void shutdown() {
        // Stopping async task from fetching layout.
        isShutdown = true;
        if (layout != null) {
            try {
                layout.cancel(true);
            } catch (Exception e) {
                log.error("Runtime shutting down. Exception in terminating fetchLayout: {}", e);
            }
        }

        stop(true);

        // Shutdown the event loop
        if (parameters.shutdownNettyEventLoop) {
            nettyEventLoop.shutdownGracefully().syncUninterruptibly();
        }
    }

    /**
     * Stop all routers associated with this runtime & disconnect them.
     */
    public void stop() {
        stop(false);
    }

    /**
     * Stop all routers associated with this Corfu Runtime.
     **/
    public void stop(boolean shutdown) {
        for (IClientRouter r : nodeRouters.values()) {
            r.stop(shutdown);
        }
        if (!shutdown) {
            // N.B. An icky side-effect of this clobbering is leaking
            // Pthreads, namely the Netty client-side worker threads.
            nodeRouters = new ConcurrentHashMap<>();
        }
    }

    /**
     * Get a UUID for a named stream.
     *
     * @param string The name of the stream.
     * @return The ID of the stream.
     */
    @SuppressWarnings("checkstyle:abbreviation")
    public static UUID getStreamID(String string) {
        return UUID.nameUUIDFromBytes(string.getBytes());
    }

    public static UUID getCheckpointStreamIdFromId(UUID streamId) {
        return getStreamID(streamId.toString() + StreamsView.CHECKPOINT_SUFFIX);
    }

    public static UUID getCheckpointStreamIdFromName(String streamName) {
        return getCheckpointStreamIdFromId(CorfuRuntime.getStreamID(streamName));
    }

    /**
     * Get corfu runtime version.
     **/
    public static String getVersionString() {
        if (Version.getVersionString().contains("SNAPSHOT")
                || Version.getVersionString().contains("source")) {
            return Version.getVersionString() + "("
                    + GitRepositoryState.getRepositoryState().commitIdAbbrev + ")";
        }
        return Version.getVersionString();
    }

    /**
     * If enabled, successful transactions will be written to a special transaction stream
     * (i.e. TRANSACTION_STREAM_ID)
     *
     * @param enable indicates if transaction logging is enabled
     * @return corfu runtime object
     */
    public CorfuRuntime setTransactionLogging(boolean enable) {
        this.getObjectsView().setTransactionLogging(enable);
        return this;
    }

    /**
     * Parse a configuration string and get a CorfuRuntime.
     *
     * @param configurationString The configuration string to parse.
     * @return A CorfuRuntime Configured based on the configuration string.
     */
    public CorfuRuntime parseConfigurationString(String configurationString) {
        // Parse comma sep. list.
        layoutServers = Pattern.compile(",")
                .splitAsStream(configurationString)
                .map(String::trim)
                .collect(Collectors.toList());
        return this;
    }

    /**
     * Add a layout server to the list of servers known by the CorfuRuntime.
     *
     * @param layoutServer A layout server to use.
     * @return A CorfuRuntime, to support the builder pattern.
     */
    public CorfuRuntime addLayoutServer(String layoutServer) {
        layoutServers.add(layoutServer);
        return this;
    }

    /**
     * Get a router, given the address.
     *
     * @param address The address of the router to get.
     * @return The router.
     */
    public IClientRouter getRouter(String address) {
        return getRouterFunction.apply(address);
    }

    /**
     * Invalidate the current layout.
     * If the layout has been previously invalidated and a new layout has not yet been retrieved,
     * this function does nothing.
     */
    public synchronized void invalidateLayout() {
        // Is there a pending request to retrieve the layout?
        if (!layout.isDone()) {
            // Don't create a new request for a layout if there is one pending.
            return;
        }
        layout = fetchLayout();
    }

    /** Check if the cluster Id of the layout matches the client cluster Id.
     *  If the client cluster Id is null, we update the client cluster Id.
     *
     *  If both the layout cluster Id and the client cluster Id is null, the check is skipped.
     *  This can only occur in the case of legacy cluster without a cluster Id.
     *
     *  @param  layout  The layout to check.
     *  @throws WrongClusterException If the layout belongs to the wrong cluster.
     */
    private void checkClusterId(@Nonnull Layout layout) {
        // We haven't adopted a clusterId yet.
        if (clusterId == null) {
            clusterId = layout.getClusterId();
            log.info("Connected to new cluster {}", clusterId == null ? "(legacy)" :
                    UuidUtils.asBase64(clusterId));
        } else if (!clusterId.equals(layout.getClusterId())) {
            // We connected but got a cluster id we didn't expect.
            throw new WrongClusterException(clusterId, layout.getClusterId());
        }
    }

    /**
     * Return a completable future which is guaranteed to contain a layout.
     * This future will continue retrying until it gets a layout.
     * If you need this completable future to fail, you should chain it with a timeout.
     *
     * @return A completable future containing a layout.
     */
    private CompletableFuture<Layout> fetchLayout() {
        return CompletableFuture.<Layout>supplyAsync(() -> {

            List<String> layoutServersCopy = new ArrayList<>(layoutServers);
            beforeRpcHandler.run();

            while (true) {

                Collections.shuffle(layoutServersCopy);
                // Iterate through the layout servers, attempting to connect to one
                for (String s : layoutServersCopy) {
                    log.debug("Trying connection to layout server {}", s);
                    try {
                        IClientRouter router = getRouter(s);
                        // Try to get a layout.
                        CompletableFuture<Layout> layoutFuture = router
                                .getClient(LayoutClient.class).getLayout();
                        // Wait for layout
                        Layout l = layoutFuture.get();

                        // If the layout we got has a smaller epoch than the router,
                        // we discard it.
                        if (l.getEpoch() < router.getEpoch()) {
                            log.warn("fetchLayout: Received a layout with epoch {} from server "
                                            + "{}:{} smaller than router epoch {}, discarded.",
                                    l.getEpoch(), router.getHost(),
                                    router.getPort(), router.getEpoch());
                            continue;
                        }

                        checkClusterId(l);

                        l.setRuntime(this);
                        // this.layout should only be assigned to the new layout future
                        // once it has been completely constructed and initialized.
                        // For example, assigning this.layout = l
                        // before setting the layout's runtime can result in other threads
                        // trying to access a layout with  a null runtime.
                        // FIXME Synchronization START
                        // We are updating multiple variables and we need the update to be
                        // synchronized across all variables.
                        // Since the variable layoutServers is used only locally within the class
                        // it is acceptable (at least the code on 10/13/2016 does not have issues)
                        // but setEpoch of routers needs to be synchronized as those variables are
                        // not local.
                        for (String server : l.getAllServers()) {
                            try {
                                getRouter(server).setEpoch(l.getEpoch());
                            } catch (NetworkException ne) {
                                // We have already received the layout and there is no need to keep
                                // client waiting.
                                // NOTE: This is true assuming this happens only at router creation.
                                // If not we also have to take care of setting the latest epoch on
                                // Client Router.
                                log.warn("fetchLayout: Error getting router : {}", server,  ne);
                            }
                        }
                        layoutServers = l.getLayoutServers();
                        layout = layoutFuture;
                        //FIXME Synchronization END

                        log.debug("Layout server {} responded with layout {}", s, l);
                        return l;
                    } catch (InterruptedException ie) {
                        throw new UnrecoverableCorfuInterruptedError(
                            "Interrupted during layout fetch", ie);
                    } catch (Exception e) {
                        log.warn("Tried to get layout from {} but failed with exception:", s, e);
                    }
                }

                log.warn("Couldn't connect to any up-to-date layout servers, retrying in {}",
                        parameters.connectionRetryRate);

                systemDownHandler.run();

                Sleep.sleepUninterruptibly(parameters.connectionRetryRate);
                if (isShutdown) {
                    return null;
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void checkVersion() {
        try {
            List<CompletableFuture<VersionInfo>> versions =
                    CFUtils.getUninterruptibly(layout).getLayoutServers()
                        .stream().map(this::getRouter)
                        .map(r -> r.getClient(BaseClient.class))
                        .map(BaseClient::getVersionInfo)
                        .collect(Collectors.toList());

            for (CompletableFuture<VersionInfo> versionCf : versions) {
                final VersionInfo version = CFUtils.getUninterruptibly(versionCf,
                        TimeoutException.class, NetworkException.class, ShutdownException.class);
                if (version.getVersion() == null) {
                    log.error("Unexpected server version, server is too old to return"
                            + " version information");
                } else if (!version.getVersion().equals(getVersionString())) {
                    log.error("connect: expected version {}, but server version is {}",
                            getVersionString(), version.getVersion());
                } else {
                    log.info("connect: client version {}, server version is {}",
                            getVersionString(), version.getVersion());
                }
            }
        } catch (TimeoutException | NetworkException | ShutdownException e) {
            log.error("connect: failed to get version", e);
        }
    }

    /**
     * Connect to the Corfu server instance.
     * When this function returns, the Corfu server is ready to be accessed.
     */
    public synchronized CorfuRuntime connect() {
        if (layout == null) {
            log.info("Connecting to Corfu server instance, layout servers={}", layoutServers);
            // Fetch the current layout and save the future.
            layout = fetchLayout();
            try {
                layout.get();
            } catch (Exception e) {
                // A serious error occurred trying to connect to the Corfu instance.
                log.error("Fatal error connecting to Corfu server instance.", e);
                throw new UnrecoverableCorfuError(e);
            }
        }

        checkVersion();

        if (parameters.isUseFastLoader()) {
            FastObjectLoader fastLoader = new FastObjectLoader(this)
                    .setBatchReadSize(parameters.getBulkReadSize())
                    .setTimeoutInMinutesForLoading((int) parameters.fastLoaderTimeout.toMinutes());
            fastLoader.loadMaps();
        }
        return this;
    }

    // Below are deprecated methods which should no longer be
    // used and may be deprecated in the future.

    // region Deprecated Constructors
    /**
     * Constructor for CorfuRuntime.
     * @deprecated Use {@link CorfuRuntime#fromParameters(CorfuRuntimeParameters)}
     **/
    @Deprecated
    public CorfuRuntime() {
        this(CorfuRuntimeParameters.builder().build());
    }

    /**
     * Parse a configuration string and get a CorfuRuntime.
     *
     * @param configurationString The configuration string to parse.
     * @deprecated Use {@link CorfuRuntime#fromParameters(CorfuRuntimeParameters)}
     */
    @Deprecated
    public CorfuRuntime(String configurationString) {
        this(CorfuRuntimeParameters.builder().build());
        this.parseConfigurationString(configurationString);
    }
    // endregion

    // region Deprecated Setters

    /**
     * Enable TLS.
     * @deprecated  Deprecated, set using {@link CorfuRuntimeParameters} instead.
     **/
    @Deprecated
    public CorfuRuntime enableTls(String keyStore, String ksPasswordFile, String trustStore,
        String tsPasswordFile) {
        log.warn("enableTls: Deprecated, please set parameters instead");
        parameters.keyStore = keyStore;
        parameters.ksPasswordFile = ksPasswordFile;
        parameters.trustStore = trustStore;
        parameters.tsPasswordFile = tsPasswordFile;
        parameters.tlsEnabled = true;
        return this;
    }

    /**
     * Enable SASL Plain Text.
     * @deprecated  Deprecated, set using {@link CorfuRuntimeParameters} instead.
     **/
    @Deprecated
    public CorfuRuntime enableSaslPlainText(String usernameFile, String passwordFile) {
        log.warn("enableSaslPlainText: Deprecated, please set parameters instead");
        parameters.usernameFile = usernameFile;
        parameters.passwordFile = passwordFile;
        parameters.saslPlainTextEnabled = true;
        return this;
    }

    /**
     * Whether or not to disable backpointers
     *
     * @param disable True, if the cache should be disabled, false otherwise.
     * @return A CorfuRuntime to support chaining.
     * @deprecated  Deprecated, set using {@link CorfuRuntimeParameters} instead.
     */
    @Deprecated
    public CorfuRuntime setBackpointersDisabled(boolean disable) {
        log.warn("setBackpointersDisabled: Deprecated, please set parameters instead");
        parameters.setBackpointersDisabled(disable);
        return this;
    }

    /**
     * Whether or not to disable the cache
     *
     * @param disable True, if the cache should be disabled, false otherwise.
     * @return A CorfuRuntime to support chaining.
     * @deprecated  Deprecated, set using {@link CorfuRuntimeParameters} instead.
     */
    @Deprecated
    public CorfuRuntime setCacheDisabled(boolean disable) {
        log.warn("setCacheDisabled: Deprecated, please set parameters instead");
        parameters.setCacheDisabled(disable);
        return this;
    }

    /**
     * Whether or not to use the fast loader.
     *
     * @param enable True, if the fast loader should be used, false otherwise.
     * @return A CorfuRuntime to support chaining.
     * @deprecated  Deprecated, set using {@link CorfuRuntimeParameters} instead.
     */
    @Deprecated
    public CorfuRuntime setLoadSmrMapsAtConnect(boolean enable) {
        log.warn("setLoadSmrMapsAtConnect: Deprecated, please set parameters instead");
        parameters.setUseFastLoader(enable);
        return this;
    }

    /**
     * Whether or not hole filling is disabled
     *
     * @param disable True, if hole filling should be disabled
     * @return A CorfuRuntime to support chaining.
     * @deprecated  Deprecated, set using {@link CorfuRuntimeParameters} instead.
     */
    @Deprecated
    public CorfuRuntime setHoleFillingDisabled(boolean disable) {
        log.warn("setHoleFillingDisabled: Deprecated, please set parameters instead");
        parameters.setHoleFillingDisabled(disable);
        return this;
    }

    /** Set the number of cache entries.
     *
     * @param numCacheEntries   The number of cache entries.
     * @deprecated  Deprecated, set using {@link CorfuRuntimeParameters} instead.
     */
    @Deprecated
    public CorfuRuntime setNumCacheEntries(long numCacheEntries) {
        log.warn("setNumCacheEntries: Deprecated, please set parameters instead");
        parameters.setNumCacheEntries(numCacheEntries);
        return this;
    }

    /** Set the cache expiration time.
     *
     * @param expiryTime   The time before cache expiration, in seconds.
     * @deprecated  Deprecated, set using {@link CorfuRuntimeParameters} instead.
     */
    @Deprecated
    public CorfuRuntime setCacheExpiryTime(int expiryTime) {
        log.warn("setCacheExpiryTime: Deprecated, please set parameters instead");
        parameters.setCacheExpiryTime(expiryTime);
        return this;
    }

    /** Set the bulk read size.
     *
     * @param size  The bulk read size.
     * @deprecated  Deprecated, set using {@link CorfuRuntimeParameters} instead.
     */
    @Deprecated
    public CorfuRuntime setBulkReadSize(int size) {
        log.warn("setBulkReadSize: Deprecated, please set parameters instead");
        parameters.setBulkReadSize(size);
        return this;
    }


    /** Set the write retry time.
     *
     * @param writeRetry   The number of times to retry writes.
     * @deprecated  Deprecated, set using {@link CorfuRuntimeParameters} instead.
     */
    @Deprecated
    public CorfuRuntime setWriteRetry(int writeRetry) {
        log.warn("setWriteRetry: Deprecated, please set parameters instead");
        parameters.setWriteRetry(writeRetry);
        return this;
    }

    /** Set the trim retry time.
     *
     * @param trimRetry   The number of times to retry on trims.
     * @deprecated  Deprecated, set using {@link CorfuRuntimeParameters} instead.
     */
    @Deprecated
    public CorfuRuntime setTrimRetry(int trimRetry) {
        log.warn("setTrimRetry: Deprecated, please set parameters instead");
        parameters.setWriteRetry(trimRetry);
        return this;
    }

    /** Set the timeout of the fast loader, in minutes.
     *
     * @param timeout   The number of minutes to wait.
     * @deprecated  Deprecated, set using {@link CorfuRuntimeParameters} instead.
     */
    @Deprecated
    public CorfuRuntime setTimeoutInMinutesForFastLoading(int timeout) {
        log.warn("setTrimRetry: Deprecated, please set parameters instead");
        parameters.setFastLoaderTimeout(Duration.ofMinutes(timeout));
        return this;
    }
    // endregion
}
