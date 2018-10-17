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
package io.atomix.protocols.raft.session;

import io.atomix.protocols.raft.service.ServiceType;

import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Raft session metadata.
 */
public final class RaftSessionMetadata {
  private final long id;
  private final String name;
  private final String type;

  public RaftSessionMetadata(long id, String name, String type) {
    this.id = id;
    this.name = checkNotNull(name, "name cannot be null");
    this.type = checkNotNull(type, "type cannot be null");
  }

  /**
   * Returns the globally unique session identifier.
   *
   * @return The globally unique session identifier.
   */
  public SessionId sessionId() {
    return SessionId.from(id);
  }

  /**
   * Returns the session name.
   *
   * @return The session name.
   */
  public String serviceName() {
    return name;
  }

  /**
   * Returns the session type.
   *
   * @return The session type.
   */
  public ServiceType serviceType() {
    return ServiceType.from(type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type, name);
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof RaftSessionMetadata) {
      RaftSessionMetadata metadata = (RaftSessionMetadata) object;
      return metadata.id == id && Objects.equals(metadata.name, name) && Objects.equals(metadata.type, type);
    }
    return false;
  }

  @Override
  public String toString() {
    return toStringHelper(this)
        .add("id", id)
        .add("name", name)
        .add("type", type)
        .toString();
  }
}
