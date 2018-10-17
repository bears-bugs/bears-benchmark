/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.oak.plugins.index;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.apache.jackrabbit.oak.spi.commit.CommitInfo;
import org.apache.jackrabbit.oak.spi.commit.Editor;
import org.apache.jackrabbit.oak.spi.commit.EditorProvider;
import org.apache.jackrabbit.oak.spi.commit.VisibleEditor;
import org.apache.jackrabbit.oak.spi.state.NodeBuilder;
import org.apache.jackrabbit.oak.spi.state.NodeState;

public class IndexUpdateProvider implements EditorProvider {

    private static final IndexUpdateCallback NOOP_CALLBACK =
            new IndexUpdateCallback() {
                @Override
                public void indexUpdate() {
                    // do nothing
                }
            };

    private final IndexEditorProvider provider;

    private final String async;

    public IndexUpdateProvider(IndexEditorProvider provider) {
        this(provider, null);
    }

    public IndexUpdateProvider(
            @Nonnull IndexEditorProvider provider, @CheckForNull String async) {
        this.provider = provider;
        this.async = async;
    }

    @Override @CheckForNull
    public Editor getRootEditor(
            NodeState before, NodeState after,
            NodeBuilder builder, CommitInfo info) {
        return VisibleEditor.wrap(new IndexUpdate(provider, async, after, builder, NOOP_CALLBACK));
    }

}
