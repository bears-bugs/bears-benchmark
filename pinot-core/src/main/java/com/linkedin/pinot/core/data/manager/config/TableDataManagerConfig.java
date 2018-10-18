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
package com.linkedin.pinot.core.data.manager.config;

import com.linkedin.pinot.common.config.TableConfig;
import com.linkedin.pinot.common.config.TableNameBuilder;
import com.linkedin.pinot.common.utils.CommonConstants.Helix.TableType;
import javax.annotation.Nonnull;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;


/**
 * The config used for TableDataManager.
 */
public class TableDataManagerConfig {
  private static final String TABLE_DATA_MANAGER_TYPE = "dataManagerType";
  private static final String TABLE_DATA_MANAGER_DATA_DIRECTORY = "directory";
  private static final String TABLE_DATA_MANAGER_CONSUMER_DIRECTORY = "consumerDirectory";
  private static final String TABLE_DATA_MANAGER_NAME = "name";
  private static final String TABLE_DATA_MANAGER_MAX_PARALLEL_SEGMENT_BUILDS = "maxParallelSegmentBuilds";

  private final Configuration _tableDataManagerConfig;

  public TableDataManagerConfig(Configuration tableDataManagerConfig)
      throws ConfigurationException {
    _tableDataManagerConfig = tableDataManagerConfig;
  }

  public Configuration getConfig() {
    return _tableDataManagerConfig;
  }

  public String getTableDataManagerType() {
    return _tableDataManagerConfig.getString(TABLE_DATA_MANAGER_TYPE);
  }

  public String getDataDir() {
    return _tableDataManagerConfig.getString(TABLE_DATA_MANAGER_DATA_DIRECTORY);
  }

  public String getConsumerDir() {
    return _tableDataManagerConfig.getString(TABLE_DATA_MANAGER_CONSUMER_DIRECTORY);
  }

  public String getTableName() {
    return _tableDataManagerConfig.getString(TABLE_DATA_MANAGER_NAME);
  }

  public int getMaxParallelSegmentBuilds() {
    return _tableDataManagerConfig.getInt(TABLE_DATA_MANAGER_MAX_PARALLEL_SEGMENT_BUILDS);
  }

  public static TableDataManagerConfig getDefaultHelixTableDataManagerConfig(
      @Nonnull InstanceDataManagerConfig instanceDataManagerConfig, @Nonnull String tableName)
      throws ConfigurationException {
    TableType tableType = TableNameBuilder.getTableTypeFromTableName(tableName);
    assert tableType != null;

    Configuration defaultConfig = new PropertiesConfiguration();
    defaultConfig.addProperty(TABLE_DATA_MANAGER_NAME, tableName);
    String dataDir = instanceDataManagerConfig.getInstanceDataDir() + "/" + tableName;
    defaultConfig.addProperty(TABLE_DATA_MANAGER_DATA_DIRECTORY, dataDir);
    defaultConfig.addProperty(TABLE_DATA_MANAGER_CONSUMER_DIRECTORY, instanceDataManagerConfig.getConsumerDir());
    defaultConfig.addProperty(TABLE_DATA_MANAGER_MAX_PARALLEL_SEGMENT_BUILDS, instanceDataManagerConfig.getMaxParallelSegmentBuilds());
    switch (tableType) {
      case OFFLINE:
        defaultConfig.addProperty(TABLE_DATA_MANAGER_TYPE, "offline");
        break;
      case REALTIME:
        defaultConfig.addProperty(TABLE_DATA_MANAGER_TYPE, "realtime");
        break;
      default:
        throw new UnsupportedOperationException("Unsupported table type for table: " + tableName);
    }

    return new TableDataManagerConfig(defaultConfig);
  }

  public void overrideConfigs(@Nonnull TableConfig tableConfig) {
    // Override table level configs

    // Currently we do not override any table level configs into TableDataManagerConfig
    // If we wish to override some table level configs using table config, override them here
    // Note: the configs in TableDataManagerConfig is immutable once the table is created, which mean it will not pick
    // up the latest table config
  }
}
