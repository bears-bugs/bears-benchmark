/**
 * Copyright (C) 2014-2016 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linkedin.pinot.core.startree;

import com.linkedin.pinot.core.segment.index.readers.Dictionary;
import java.io.Closeable;
import java.util.List;
import java.util.Map;


/**
 * Interface for StarTree.
 */
public interface StarTree extends Closeable {

  /**
   * Get the root node of the star tree.
   */
  StarTreeNode getRoot();

  /**
   * Get a list of all dimension names. The node dimension id is the index of the dimension name in this list.
   */
  List<String> getDimensionNames();

  /**
   * Print the tree.
   */
  void printTree(Map<String, Dictionary> dictionaryMap);
}
