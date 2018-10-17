/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.jackrabbit.oak.plugins.observation;

import static org.apache.jackrabbit.oak.api.CommitFailedException.OAK;

import javax.annotation.Nonnull;

import org.apache.jackrabbit.oak.api.CommitFailedException;
import org.apache.jackrabbit.oak.spi.commit.CommitHook;
import org.apache.jackrabbit.oak.spi.commit.CommitInfo;
import org.apache.jackrabbit.oak.spi.state.NodeState;
import org.apache.jackrabbit.oak.stats.Clock;

/**
 * This {@code CommitHook} can be used to block or delay commits for any length of time.
 * As long as commits are blocked this hook throws a {@code CommitFailedException}.
 */
public class CommitRateLimiter implements CommitHook {
    private volatile boolean blockCommits;
    private volatile long delay;

    /**
     * Block any further commits until {@link #unblockCommits()} is called.
     */
    public void blockCommits() {
        blockCommits = true;
    }

    /**
     * Unblock blocked commits.
     */
    public void unblockCommits() {
        blockCommits = false;
    }

    /**
     * Number of milli seconds to delay commits going through this hook.
     * If {@code 0}, any currently blocked commit will be unblocked.
     * @param delay  milli seconds
     */
    public void setDelay(long delay) {
        this.delay = delay;
        if (delay == 0) {
            synchronized (this) {
                this.notifyAll();
            }
        }
    }

    @Nonnull
    @Override
    public NodeState processCommit(NodeState before, NodeState after, CommitInfo info)
            throws CommitFailedException {
        if (blockCommits) {
            throw new CommitFailedException(OAK, 1, "System busy. Try again later.");
        }
        delay();
        return after;
    }

    private void delay() throws CommitFailedException {
        if (delay > 0) {
            synchronized (this) {
                try {
                    long t0 = Clock.ACCURATE.getTime();
                    long dt = delay;
                    do {
                        wait(dt);
                        dt = dt - Clock.ACCURATE.getTime() + t0;
                    } while (delay > 0 && dt > 0);
                } catch (InterruptedException e) {
                    throw new CommitFailedException(OAK, 2, "Interrupted while waiting to commit", e);
                }
            }
        }
    }
}
