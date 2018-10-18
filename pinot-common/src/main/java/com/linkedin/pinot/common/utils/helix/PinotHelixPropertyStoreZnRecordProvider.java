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
package com.linkedin.pinot.common.utils.helix;

import org.apache.helix.AccessOption;
import org.apache.helix.ZNRecord;
import org.apache.helix.store.zk.ZkHelixPropertyStore;


public class PinotHelixPropertyStoreZnRecordProvider {

  private final ZkHelixPropertyStore<ZNRecord> propertyStore;
  private final String pathPrefix;

  private PinotHelixPropertyStoreZnRecordProvider() {
    this.pathPrefix = null;
    this.propertyStore = null;
  }

  private PinotHelixPropertyStoreZnRecordProvider(ZkHelixPropertyStore<ZNRecord> propertyStore, String relativePathName) {
    this.propertyStore = propertyStore;
    this.pathPrefix = relativePathName;
  }

  public static PinotHelixPropertyStoreZnRecordProvider forSchema(ZkHelixPropertyStore<ZNRecord> propertyStore) {
    return new PinotHelixPropertyStoreZnRecordProvider(propertyStore, "/SCHEMAS");
  }

  public static PinotHelixPropertyStoreZnRecordProvider forTable(ZkHelixPropertyStore<ZNRecord> propertyStore) {
    return new PinotHelixPropertyStoreZnRecordProvider(propertyStore, "/CONFIGS/TABLES");
  }

  public static PinotHelixPropertyStoreZnRecordProvider forSegments(ZkHelixPropertyStore<ZNRecord> propertyStore) {
    return new PinotHelixPropertyStoreZnRecordProvider(propertyStore, "/SEGMENTS");
  }

  public ZNRecord get(String name) {
    return propertyStore.get(pathPrefix + "/" + name, null, AccessOption.PERSISTENT);
  }

  public void set(String name, ZNRecord record) {
    propertyStore.set(pathPrefix + "/" + name, record, AccessOption.PERSISTENT);
  }

  public boolean exist(String path) {
    return propertyStore.exists(pathPrefix + "/" + path, AccessOption.PERSISTENT);
  }

  public String getRelativePath() {
    return pathPrefix;
  }
}
