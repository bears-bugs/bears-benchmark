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
package com.linkedin.pinot.common.config;

import com.linkedin.pinot.common.utils.ControllerTenantNameBuilder;
import org.apache.helix.HelixManager;


/**
 * Wrapper class over TableConfig
 * This class will help answer questions about what are consuming/completed tags for a table
 */
public class RealtimeTagConfig {

  private TableConfig _tableConfig;

  private String _serverTenant;
  private String _consumingRealtimeServerTag;
  private String _completedRealtimeServerTag;

  private boolean _moveCompletedSegments = false;

  public RealtimeTagConfig(TableConfig realtimeTableConfig, HelixManager helixManager) {

    _tableConfig = realtimeTableConfig;

    // TODO: we will introduce TENANTS config in property store, which should return the consuming/completed tags
    // once we have that, below code will change to fetching TENANT from property store and returning the consuming/completed values
    _serverTenant = realtimeTableConfig.getTenantConfig().getServer();
    _consumingRealtimeServerTag = ControllerTenantNameBuilder.getRealtimeTenantNameForTenant(_serverTenant);
    _completedRealtimeServerTag = ControllerTenantNameBuilder.getRealtimeTenantNameForTenant(_serverTenant);
    if (!_consumingRealtimeServerTag.equals(_completedRealtimeServerTag)) {
      _moveCompletedSegments = true;
    }
  }

  public TableConfig getTableConfig() {
    return _tableConfig;
  }

  public String getConsumingRealtimeServerTag() {
    return _consumingRealtimeServerTag;
  }

  public String getCompletedRealtimeServerTag() {
    return _completedRealtimeServerTag;
  }

  public String getServerTenantName() {
    return _serverTenant;
  }

  public boolean isMoveCompletedSegments() {
    return _moveCompletedSegments;
  }
}

