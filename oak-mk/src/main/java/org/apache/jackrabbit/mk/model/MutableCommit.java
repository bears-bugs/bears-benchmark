/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.mk.model;

/**
 *
 */
public class MutableCommit extends AbstractCommit {

    /**
     * Commit id.
     */
    private Id id;
    
    public MutableCommit() {
    }

    /**
     * Copy constructor.
     * 
     * @param other other commit
     */
    public MutableCommit(StoredCommit other) {
        setParentId(other.getParentId());
        setRootNodeId(other.getRootNodeId());
        setCommitTS(other.getCommitTS());
        setMsg(other.getMsg());
        setChanges(other.getChanges());
        setBranchRootId(other.getBranchRootId());
        this.id = other.getId();
    }

    public void setParentId(Id parentId) {
        this.parentId = parentId;
    }

    public void setRootNodeId(Id rootNodeId) {
        this.rootNodeId = rootNodeId;
    }

    public void setCommitTS(long commitTS) {
        this.commitTS = commitTS;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public void setBranchRootId(Id branchRootId) {
        this.branchRootId = branchRootId;
    }

    /**
     * Return the commit id.
     * 
     * @return commit id
     */
    public Id getId() {
        return id;
    }
}
