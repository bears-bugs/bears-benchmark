/*
 * Copyright 2011-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lettuce.core.cluster.models.partitions;

import java.io.Serializable;
import java.util.*;

import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.SlotHash;
import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.models.role.RedisNodeDescription;

/**
 * Representation of a Redis Cluster node. A {@link RedisClusterNode} is identified by its {@code nodeId}.
 * <p/>
 * A {@link RedisClusterNode} can be a {@link #getRole() responsible master} or slave. Masters can be responsible for zero to
 * {@link io.lettuce.core.cluster.SlotHash#SLOT_COUNT 16384} slots. Each slave refers to exactly one {@link #getSlaveOf()
 * master}. Nodes can have different {@link io.lettuce.core.cluster.models.partitions.RedisClusterNode.NodeFlag flags} assigned.
 * <p/>
 * This class is mutable and not thread-safe if mutated by multiple threads concurrently.
 *
 * @author Mark Paluch
 * @since 3.0
 */
@SuppressWarnings("serial")
public class RedisClusterNode implements Serializable, RedisNodeDescription {

    private RedisURI uri;
    private String nodeId;

    private boolean connected;
    private String slaveOf;
    private long pingSentTimestamp;
    private long pongReceivedTimestamp;
    private long configEpoch;

    private BitSet slots;
    private final Set<NodeFlag> flags = EnumSet.noneOf(NodeFlag.class);
    private final List<RedisURI> aliases = new ArrayList<>();

    public RedisClusterNode() {
    }

    public RedisClusterNode(RedisURI uri, String nodeId, boolean connected, String slaveOf, long pingSentTimestamp,
            long pongReceivedTimestamp, long configEpoch, List<Integer> slots, Set<NodeFlag> flags) {

        this.uri = uri;
        this.nodeId = nodeId;
        this.connected = connected;
        this.slaveOf = slaveOf;
        this.pingSentTimestamp = pingSentTimestamp;
        this.pongReceivedTimestamp = pongReceivedTimestamp;
        this.configEpoch = configEpoch;

        setSlotBits(slots);
        setFlags(flags);
    }

    public RedisClusterNode(RedisClusterNode redisClusterNode) {

        LettuceAssert.notNull(redisClusterNode, "RedisClusterNode must not be null");

        this.uri = redisClusterNode.uri;
        this.nodeId = redisClusterNode.nodeId;
        this.connected = redisClusterNode.connected;
        this.slaveOf = redisClusterNode.slaveOf;
        this.pingSentTimestamp = redisClusterNode.pingSentTimestamp;
        this.pongReceivedTimestamp = redisClusterNode.pongReceivedTimestamp;
        this.configEpoch = redisClusterNode.configEpoch;
        this.aliases.addAll(redisClusterNode.aliases);

        if (redisClusterNode.slots != null && !redisClusterNode.slots.isEmpty()) {
            this.slots = new BitSet(SlotHash.SLOT_COUNT);
            this.slots.or(redisClusterNode.slots);
        }

        setFlags(redisClusterNode.flags);
    }

    /**
     * Create a new instance of {@link RedisClusterNode} by passing the {@code nodeId}
     *
     * @param nodeId the nodeId
     * @return a new instance of {@link RedisClusterNode}
     */
    public static RedisClusterNode of(String nodeId) {

        LettuceAssert.notNull(nodeId, "NodeId must not be null");

        RedisClusterNode redisClusterNode = new RedisClusterNode();
        redisClusterNode.setNodeId(nodeId);

        return redisClusterNode;
    }

    @Override
    public RedisClusterNode clone() {
        return new RedisClusterNode(this);
    }

    public RedisURI getUri() {
        return uri;
    }

    /**
     * Sets the connection point details. Usually the host/ip/port where a particular Redis Cluster node server is running.
     *
     * @param uri the {@link RedisURI}, must not be {@literal null}
     */
    public void setUri(RedisURI uri) {

        LettuceAssert.notNull(uri, "RedisURI must not be null");
        this.uri = uri;
    }

    public String getNodeId() {
        return nodeId;
    }

    /**
     * Sets {@code nodeId}.
     *
     * @param nodeId the {@code nodeId}
     */
    public void setNodeId(String nodeId) {
        LettuceAssert.notNull(nodeId, "NodeId must not be null");
        this.nodeId = nodeId;
    }

    public boolean isConnected() {
        return connected;
    }

    /**
     * Sets the {@code connected} flag. The {@code connected} flag describes whether the node which provided details about the
     * node is connected to the particular {@link RedisClusterNode}.
     *
     * @param connected the {@code connected} flag
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getSlaveOf() {
        return slaveOf;
    }

    /**
     * Sets the replication source.
     *
     * @param slaveOf the replication source, can be {@literal null}
     */
    public void setSlaveOf(String slaveOf) {
        this.slaveOf = slaveOf;
    }

    public long getPingSentTimestamp() {
        return pingSentTimestamp;
    }

    /**
     * Sets the last {@code pingSentTimestamp}.
     *
     * @param pingSentTimestamp the last {@code pingSentTimestamp}
     */
    public void setPingSentTimestamp(long pingSentTimestamp) {
        this.pingSentTimestamp = pingSentTimestamp;
    }

    public long getPongReceivedTimestamp() {
        return pongReceivedTimestamp;
    }

    /**
     * Sets the last {@code pongReceivedTimestamp}.
     *
     * @param pongReceivedTimestamp the last {@code pongReceivedTimestamp}
     */
    public void setPongReceivedTimestamp(long pongReceivedTimestamp) {
        this.pongReceivedTimestamp = pongReceivedTimestamp;
    }

    public long getConfigEpoch() {
        return configEpoch;
    }

    /**
     * Sets the {@code configEpoch}.
     *
     * @param configEpoch the {@code configEpoch}
     */
    public void setConfigEpoch(long configEpoch) {
        this.configEpoch = configEpoch;
    }

    public List<Integer> getSlots() {

        if (slots == null || slots.isEmpty()) {
            return Collections.emptyList();
        }

        List<Integer> slots = new ArrayList<>();

        for (int i = 0; i < SlotHash.SLOT_COUNT; i++) {

            if (this.slots.get(i)) {
                slots.add(i);
            }
        }

        return slots;
    }

    /**
     * Sets the list of slots for which this {@link RedisClusterNode} is the
     * {@link io.lettuce.core.cluster.models.partitions.RedisClusterNode.NodeFlag#MASTER}. The list is empty if this node is not
     * a master or the node is not responsible for any slots at all.
     *
     * @param slots list of slots, must not be {@literal null} but may be empty
     */
    public void setSlots(List<Integer> slots) {

        LettuceAssert.notNull(slots, "Slots must not be null");

        setSlotBits(slots);
    }

    private void setSlotBits(List<Integer> slots) {

        if (slots.isEmpty() && this.slots == null) {
            return;
        }

        if (this.slots == null) {
            this.slots = new BitSet(SlotHash.SLOT_COUNT);
        }

        this.slots.clear();

        for (Integer slot : slots) {
            this.slots.set(slot);
        }
    }

    public Set<NodeFlag> getFlags() {
        return flags;
    }

    /**
     * Set of {@link io.lettuce.core.cluster.models.partitions.RedisClusterNode.NodeFlag node flags}.
     *
     * @param flags the set of node flags.
     */
    public void setFlags(Set<NodeFlag> flags) {

        this.flags.clear();
        this.flags.addAll(flags);
    }

    /**
     * @param nodeFlag the node flag
     * @return true if the {@linkplain NodeFlag} is contained within the flags.
     */
    public boolean is(NodeFlag nodeFlag) {
        return getFlags().contains(nodeFlag);
    }

    /**
     * Add an alias to {@link RedisClusterNode}.
     *
     * @param alias must not be {@literal null}.
     */
    public void addAlias(RedisURI alias) {

        LettuceAssert.notNull(alias, "Alias URI must not be null");
        this.aliases.add(alias);
    }

    public List<RedisURI> getAliases() {
        return aliases;
    }

    /**
     * @param slot the slot hash
     * @return true if the slot is contained within the handled slots.
     */
    public boolean hasSlot(int slot) {
        return slot <= SlotHash.SLOT_COUNT && this.slots != null && this.slots.get(slot);
    }

    /**
     * Returns the {@link Role} of the Redis Cluster node based on the {@link #getFlags() flags}.
     *
     * @return the Redis Cluster node role
     */
    @Override
    public Role getRole() {
        return is(NodeFlag.MASTER) ? Role.MASTER : Role.SLAVE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RedisClusterNode)) {
            return false;
        }

        RedisClusterNode that = (RedisClusterNode) o;

        if (nodeId != null ? !nodeId.equals(that.nodeId) : that.nodeId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return 31 * (nodeId != null ? nodeId.hashCode() : 0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [uri=").append(uri);
        sb.append(", nodeId='").append(nodeId).append('\'');
        sb.append(", connected=").append(connected);
        sb.append(", slaveOf='").append(slaveOf).append('\'');
        sb.append(", pingSentTimestamp=").append(pingSentTimestamp);
        sb.append(", pongReceivedTimestamp=").append(pongReceivedTimestamp);
        sb.append(", configEpoch=").append(configEpoch);
        sb.append(", flags=").append(flags);
        sb.append(", aliases=").append(aliases);
        if (slots != null) {
            sb.append(", slot count=").append(slots.cardinality());
        }
        sb.append(']');
        return sb.toString();
    }

    /**
     * Redis Cluster node flags.
     */
    public enum NodeFlag {
        NOFLAGS, MYSELF, SLAVE, MASTER, EVENTUAL_FAIL, FAIL, HANDSHAKE, NOADDR;
    }
}
