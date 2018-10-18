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

import com.linkedin.pinot.common.request.BrokerRequest;


/**
 * AST node for a list of columns.
 */
public class OutputColumnListAstNode extends BaseAstNode {
  private boolean _star;

  public boolean isStar() {
    return _star;
  }

  @Override
  public void addChild(AstNode childNode) {
    if (childNode instanceof StarColumnListAstNode) {
      _star = true;
    } else {
      super.addChild(childNode);
    }
  }

  @Override
  public void updateBrokerRequest(BrokerRequest brokerRequest) {
    sendBrokerRequestUpdateToChildren(brokerRequest);
  }
}
