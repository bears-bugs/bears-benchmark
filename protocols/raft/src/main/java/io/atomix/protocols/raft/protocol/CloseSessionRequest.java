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
package io.atomix.protocols.raft.protocol;

/**
 * Close session request.
 */
public class CloseSessionRequest extends SessionRequest {

  /**
   * Returns a new unregister request builder.
   *
   * @return A new unregister request builder.
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  public CloseSessionRequest(long session) {
    super(session);
  }

  /**
   * Unregister request builder.
   */
  public static class Builder extends SessionRequest.Builder<Builder, CloseSessionRequest> {
    @Override
    public CloseSessionRequest build() {
      validate();
      return new CloseSessionRequest(session);
    }
  }
}
