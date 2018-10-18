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
package com.linkedin.pinot.controller.helix.core.minion.generator;

import com.clearspring.analytics.util.Preconditions;
import com.linkedin.pinot.controller.helix.core.minion.ClusterInfoProvider;
import com.linkedin.pinot.controller.helix.core.minion.PinotHelixTaskResourceManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * Registry for all {@link PinotTaskGenerator}.
 */
public class TaskGeneratorRegistry {
  private final Map<String, PinotTaskGenerator> _taskGeneratorRegistry = new HashMap<>();

  public TaskGeneratorRegistry(@Nonnull ClusterInfoProvider clusterInfoProvider) {
    registerTaskGenerator(new ConvertToRawIndexTaskGenerator(clusterInfoProvider));
  }

  /**
   * Register a task generator.
   *
   * @param pinotTaskGenerator Task generator to be registered
   */
  public void registerTaskGenerator(@Nonnull PinotTaskGenerator pinotTaskGenerator) {
    // Task type cannot contain the task name separator
    String taskType = pinotTaskGenerator.getTaskType();
    Preconditions.checkArgument(!taskType.contains(PinotHelixTaskResourceManager.TASK_NAME_SEPARATOR),
        "Task type: %s cannot contain underscore character", taskType);

    _taskGeneratorRegistry.put(taskType, pinotTaskGenerator);
  }

  /**
   * Get all registered task types.
   *
   * @return Set of all registered task types
   */
  @Nonnull
  public Set<String> getAllTaskTypes() {
    return _taskGeneratorRegistry.keySet();
  }

  /**
   * Get the task generator for the given task type.
   *
   * @param taskType Task type
   * @return Task generator for the given task type
   */
  @Nullable
  public PinotTaskGenerator getTaskGenerator(@Nonnull String taskType) {
    return _taskGeneratorRegistry.get(taskType);
  }
}
