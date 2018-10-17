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
package org.apache.jackrabbit.oak.plugins.document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

/**
 * The JournalGarbageCollector can clean up JournalEntries that are older than a
 * particular age.
 * <p/>
 * It would typically be invoked in conjunction with the VersionGarbageCollector
 * but must not be confused with that one - 'journal' refers to the separate
 * collection that contains changed paths per background writes used for
 * observation.
 */
public class JournalGarbageCollector {

    //copied from VersionGarbageCollector:
    private static final int DELETE_BATCH_SIZE = 450;

    private final DocumentStore ds;

    private static final Logger log = LoggerFactory.getLogger(JournalGarbageCollector.class);

    public JournalGarbageCollector(DocumentNodeStore nodeStore) {
        this.ds = nodeStore.getDocumentStore();
    }

    /**
     * Deletes entries in the journal that are older than the given
     * maxRevisionAge.
     *
     * @param maxRevisionAge entries older than this age will be removed
     * @param unit           the timeunit for maxRevisionAge
     * @return the number of entries that have been removed
     */
    public int gc(long maxRevisionAge, TimeUnit unit) {
        long maxRevisionAgeInMillis = unit.toMillis(maxRevisionAge);
        if (log.isDebugEnabled()) {
            log.debug("gc: Journal garbage collection starts with maxAge: {} min.", TimeUnit.MILLISECONDS.toMinutes(maxRevisionAgeInMillis));
        }
        Stopwatch sw = Stopwatch.createStarted();

        // the journal has ids of the following format:
        // 1-0000014db9aaf710-00000001
        // whereas the first number is the cluster node id.
        // now, this format prevents from doing a generic
        // query to get all 'old' entries, as the documentstore
        // can only query for a sequential list of entries.
        // (and the cluster node id here partitions the set
        // of entries that we have to delete)
        // To account for that, we simply iterate over all 
        // cluster node ids and clean them up individually.
        // Note that there are possible alternatives, such
        // as: let each node clean up its own old entries
        // but the chosen path is also quite simple: it can
        // be started on any instance - but best on only one.
        // if it's run on multiple concurrently, then they
        // will compete at deletion, which is not optimal
        // due to performance, but does not harm.

        // 1. get the list of cluster node ids
        final List<ClusterNodeInfoDocument> clusterNodeInfos = ClusterNodeInfoDocument.all(ds);
        int numDeleted = 0;
        for (ClusterNodeInfoDocument clusterNodeInfoDocument : clusterNodeInfos) {
            // current algorithm is to simply look at all cluster nodes
            // irrespective of whether they are active or inactive etc.
            // this could be optimized for inactive ones: at some point, all
            // journal entries of inactive ones would have been cleaned up
            // and at that point we could stop including those long-time-inactive ones.
            // that 'long time' aspect would have to be tracked though, to be sure
            // we don't leave garbage.
            // so simpler is to quickly do a query even for long-time inactive ones
            final int clusterNodeId = clusterNodeInfoDocument.getClusterId();

            // 2. iterate over that list and do a query with
            //    a limit of 'batch size'
            boolean branch = false;
            long startPointer = 0;
            while (true) {
                String fromKey = JournalEntry.asId(new Revision(startPointer, 0, clusterNodeId, branch));
                String toKey = JournalEntry.asId(new Revision(System.currentTimeMillis() - maxRevisionAgeInMillis, Integer.MAX_VALUE, clusterNodeId, branch));
                int limit = DELETE_BATCH_SIZE;
                List<JournalEntry> deletionBatch = ds.query(Collection.JOURNAL, fromKey, toKey, limit);
                if (deletionBatch.size() > 0) {
                    ds.remove(Collection.JOURNAL, asKeys(deletionBatch));
                    numDeleted += deletionBatch.size();
                }
                if (deletionBatch.size() < limit) {
                    if (!branch) {
                        // do the same for branches:
                        // this will start at the beginning again with branch set to true
                        // and eventually finish too
                        startPointer = 0;
                        branch = true;
                        continue;
                    }
                    break;
                }
                startPointer = deletionBatch.get(deletionBatch.size() - 1).getRevisionTimestamp();
            }
        }

        sw.stop();

        log.info("gc: Journal garbage collection took {}, deleted {} entries that were older than {} min.", sw, numDeleted, TimeUnit.MILLISECONDS.toMinutes(maxRevisionAgeInMillis));
        return numDeleted;
    }

    private List<String> asKeys(List<JournalEntry> deletionBatch) {
        final List<String> keys = new ArrayList<String>(deletionBatch.size());
        for (JournalEntry e : deletionBatch) {
            keys.add(e.getId());
        }
        return keys;
    }

}
