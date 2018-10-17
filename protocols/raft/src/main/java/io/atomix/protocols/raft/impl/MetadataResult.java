/*
 * Copyright 2017-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atomix.protocols.raft.impl;

import io.atomix.primitive.session.SessionMetadata;

import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Metadata result.
 */
public final class MetadataResult {
  final Set<SessionMetadata> sessions;

  MetadataResult(Set<SessionMetadata> sessions) {
    this.sessions = sessions;
  }

  /**
   * Returns the session metadata.
   *
   * @return The session metadata.
   */
  public Set<SessionMetadata> sessions() {
    return sessions;
  }

  @Override
  public String toString() {
    return toStringHelper(this)
        .add("sessions", sessions)
        .toString();
  }
}
