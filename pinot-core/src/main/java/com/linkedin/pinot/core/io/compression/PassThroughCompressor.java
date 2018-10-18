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
package com.linkedin.pinot.core.io.compression;

import java.io.IOException;
import java.nio.ByteBuffer;


/**
 * A pass-through implementation of {@link ChunkCompressor}, that simply returns the input uncompressed data
 * with performing any compression. This is useful in cases where cost of de-compression out-weighs benefit of
 * compression.
 */
public class PassThroughCompressor implements ChunkCompressor {

  @Override
  public int compress(ByteBuffer inUncompressed, ByteBuffer outCompressed)
      throws IOException {
    outCompressed.put(inUncompressed);

    // Make the output ByteBuffer read for read.
    outCompressed.flip();
    return outCompressed.limit();
  }
}
