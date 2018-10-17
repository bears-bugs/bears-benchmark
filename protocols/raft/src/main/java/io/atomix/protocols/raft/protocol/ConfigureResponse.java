/*
 * Copyright 2015-present Open Networking Foundation
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
 * limitations under the License
 */
package io.atomix.protocols.raft.protocol;

import io.atomix.protocols.raft.RaftError;

/**
 * Configuration installation response.
 */
public class ConfigureResponse extends AbstractRaftResponse {

  /**
   * Returns a new configure response builder.
   *
   * @return A new configure response builder.
   */
  public static Builder builder() {
    return new Builder();
  }

  public ConfigureResponse(Status status, RaftError error) {
    super(status, error);
  }

  /**
   * Heartbeat response builder.
   */
  public static class Builder extends AbstractRaftResponse.Builder<Builder, ConfigureResponse> {
    @Override
    public ConfigureResponse build() {
      return new ConfigureResponse(status, error);
    }
  }
}
