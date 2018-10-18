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
package com.linkedin.pinot.pql.parsers.pql2.ast;

/**
 * AST node for LIMIT clauses.
 */
public class LimitAstNode extends BaseAstNode {
  private final int _count;
  private final int _offset;

  public LimitAstNode(int count) {
    _count = count;
    _offset = 0;
  }

  public LimitAstNode(int count, int offset) {
    _count = count;
    _offset = offset;
  }

  public int getCount() {
    return _count;
  }

  public int getOffset() {
    return _offset;
  }

  @Override
  public String toString() {
    return "LimitAstNode{" +
        "_count=" + _count +
        ", _offset=" + _offset +
        '}';
  }
}
