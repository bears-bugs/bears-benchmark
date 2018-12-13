package com.ctrip.framework.apollo;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ctrip.framework.apollo.tracer.internals.MockMessageProducerManager;
import com.ctrip.framework.apollo.tracer.spi.MessageProducer;
import com.ctrip.framework.apollo.tracer.spi.Transaction;
import java.io.IOException;
import java.net.ServerSocket;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.junit.After;
import org.junit.Before;

public abstract class BaseIntegrationTest {
  protected static final int PORT = findFreePort();
  private Server server;

  /**
   * init and start a jetty server, remember to call server.stop when the task is finished
   */
  protected Server startServerWithHandlers(ContextHandler... handlers) throws Exception {
    server = new Server(PORT);

    ContextHandlerCollection contexts = new ContextHandlerCollection();
    contexts.setHandlers(handlers);

    server.setHandler(contexts);
    server.start();

    return server;
  }


  @Before
  public void setUp() throws Exception {
    MessageProducer someProducer = mock(MessageProducer.class);
    MockMessageProducerManager.setProducer(someProducer);

    Transaction someTransaction = mock(Transaction.class);

    when(someProducer.newTransaction(anyString(), anyString())).thenReturn(someTransaction);
  }

  @After
  public void tearDown() throws Exception {
    if (server != null && server.isStarted()) {
      server.stop();
    }
  }

  protected ContextHandler mockServerHandler(final int statusCode, final String response) {
    ContextHandler context = new ContextHandler("/");
    context.setHandler(new AbstractHandler() {

      @Override
      public void handle(String target, Request baseRequest, HttpServletRequest request,
          HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("text/plain;charset=UTF-8");
        response.setStatus(statusCode);
        response.getWriter().println(response);
        baseRequest.setHandled(true);
      }
    });
    return context;
  }

  /**
   * Returns a free port number on localhost.
   *
   * Heavily inspired from org.eclipse.jdt.launching.SocketUtil (to avoid a dependency to JDT just because of this).
   * Slightly improved with close() missing in JDT. And throws exception instead of returning -1.
   *
   * @return a free port number on localhost
   * @throws IllegalStateException if unable to find a free port
   */
  protected static int findFreePort() {
    ServerSocket socket = null;
    try {
      socket = new ServerSocket(0);
      socket.setReuseAddress(true);
      int port = socket.getLocalPort();
      try {
        socket.close();
      } catch (IOException e) {
        // Ignore IOException on close()
      }
      return port;
    } catch (IOException e) {
    } finally {
      if (socket != null) {
        try {
          socket.close();
        } catch (IOException e) {
        }
      }
    }
    throw new IllegalStateException("Could not find a free TCP/IP port to start embedded Jetty HTTP Server on");
  }
}
