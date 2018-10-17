/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.servicecomb.codec.protobuf.utils.schema;

import java.io.IOException;

import org.apache.servicecomb.common.javassist.SingleWrapper;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;

public class NormalWrapSchema extends AbstractWrapSchema {

  @SuppressWarnings("unchecked")
  public NormalWrapSchema(Schema<?> schema) {
    this.schema = (Schema<Object>) schema;
  }

  @Override
  public Object readFromEmpty() {
    SingleWrapper wrapper = (SingleWrapper) schema.newMessage();
    return wrapper.readField();
  }

  public Object readObject(Input input) throws IOException {
    SingleWrapper wrapper = (SingleWrapper) schema.newMessage();
    schema.mergeFrom(input, wrapper);

    return wrapper.readField();
  }

  public void writeObject(Output output, Object value) throws IOException {
    if (value == null) {
      return;
    }

    SingleWrapper wrapper = (SingleWrapper) schema.newMessage();
    wrapper.writeField(value);

    schema.writeTo(output, wrapper);
  }
}
