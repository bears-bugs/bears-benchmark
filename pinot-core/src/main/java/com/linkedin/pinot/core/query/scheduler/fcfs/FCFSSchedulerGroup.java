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
package com.linkedin.pinot.core.query.scheduler.fcfs;

import com.linkedin.pinot.core.query.scheduler.AbstractSchedulerGroup;
import com.linkedin.pinot.core.query.scheduler.SchedulerGroup;
import com.linkedin.pinot.core.query.scheduler.SchedulerGroupAccountant;
import com.linkedin.pinot.core.query.scheduler.SchedulerQueryContext;
import javax.annotation.Nonnull;


public class FCFSSchedulerGroup extends AbstractSchedulerGroup {

  public FCFSSchedulerGroup(@Nonnull String group) {
    super(group);
  }

  /**
   * Group that has pending query with earlier arrival time has higher priority.
   * @param rhs
   * @return 1 if this has lower arrival time than rhs
   *         -1 if this has higher arrival time than lhs
   *         0 if arrival times are equal
   */
  @Override
  public int compareTo(SchedulerGroupAccountant rhs) {
    return compareTo(this, ((SchedulerGroup) rhs));
  }

  public static int compareTo(SchedulerGroup lhs, SchedulerGroup rhs) {
    if (rhs == null) {
      return 1;
    }

    if (lhs == rhs) {
      return 0;
    }

    SchedulerQueryContext lhsFirst = lhs.peekFirst();
    SchedulerQueryContext rhsFirst = rhs.peekFirst();
    if (lhsFirst != null && rhsFirst != null) {
      long lhsArrivalMs = lhsFirst.getArrivalTimeMs();
      long rhsArrivalMs = rhsFirst.getArrivalTimeMs();
      if (lhsArrivalMs < rhsArrivalMs) {
        return 1;
      } else if (lhsArrivalMs > rhsArrivalMs) {
        return -1;
      }
      return 0;
    } else if (lhsFirst != null && rhsFirst == null) {
      return 1;
    } else if (lhsFirst == null && rhsFirst != null) {
      return -1;
    } else {
      return 0;
    }
  }
}
