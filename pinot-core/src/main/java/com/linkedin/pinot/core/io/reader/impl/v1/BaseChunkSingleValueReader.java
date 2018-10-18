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
package com.linkedin.pinot.core.io.reader.impl.v1;

import com.linkedin.pinot.core.io.compression.ChunkCompressorFactory;
import com.linkedin.pinot.core.io.compression.ChunkDecompressor;
import com.linkedin.pinot.core.io.reader.BaseSingleColumnSingleValueReader;
import com.linkedin.pinot.core.io.reader.impl.ChunkReaderContext;
import com.linkedin.pinot.core.segment.memory.PinotDataBuffer;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Abstract class implementation for {@link BaseSingleColumnSingleValueReader}.
 * Base class for the fixed and variable byte reader implementations.
 *
 */
public abstract class BaseChunkSingleValueReader extends BaseSingleColumnSingleValueReader<ChunkReaderContext> {
  private static final Logger LOGGER = LoggerFactory.getLogger(BaseChunkSingleValueReader.class);

  protected static final int INT_SIZE = Integer.SIZE / Byte.SIZE;
  protected static final int LONG_SIZE = Long.SIZE / Byte.SIZE;
  protected static final int FLOAT_SIZE = Float.SIZE / Byte.SIZE;
  protected static final int DOUBLE_SIZE = Double.SIZE / Byte.SIZE;

  protected final PinotDataBuffer _dataBuffer;
  protected final PinotDataBuffer _dataHeader;
  protected final int _chunkSize;
  private final PinotDataBuffer _rawData;
  private final boolean _isCompressed;
  protected ChunkDecompressor _chunkDecompressor;

  protected final int _numDocsPerChunk;
  protected final int _numChunks;
  protected final int _lengthOfLongestEntry;

  /**
   * Constructor for the class.
   *
   * @param pinotDataBuffer Data buffer
   */
  public BaseChunkSingleValueReader(PinotDataBuffer pinotDataBuffer) {
    _dataBuffer = pinotDataBuffer;

    int headerOffset = 0;
    int version = _dataBuffer.getInt(headerOffset);
    headerOffset += INT_SIZE;

    _numChunks = _dataBuffer.getInt(headerOffset);
    headerOffset += INT_SIZE;

    _numDocsPerChunk = _dataBuffer.getInt(headerOffset);
    headerOffset += INT_SIZE;

    _lengthOfLongestEntry = _dataBuffer.getInt(headerOffset);
    headerOffset += INT_SIZE;

    int dataHeaderStart = headerOffset;
    if (version > 1) {
      _dataBuffer.getInt(headerOffset); // Total docs
      headerOffset += INT_SIZE;

      ChunkCompressorFactory.CompressionType compressionType =
          ChunkCompressorFactory.CompressionType.values()[_dataBuffer.getInt(headerOffset)];
      _chunkDecompressor = ChunkCompressorFactory.getDecompressor(compressionType);
      _isCompressed = !compressionType.equals(ChunkCompressorFactory.CompressionType.PASS_THROUGH);

      headerOffset += INT_SIZE;
      dataHeaderStart = _dataBuffer.getInt(headerOffset);
    } else {
      _isCompressed = true;
      _chunkDecompressor = ChunkCompressorFactory.getDecompressor(ChunkCompressorFactory.CompressionType.SNAPPY);
    }

    _chunkSize = (_lengthOfLongestEntry * _numDocsPerChunk);

    // Slice out the header from the data buffer.
    int dataHeaderLength = _numChunks * INT_SIZE;
    int rawDataStart = dataHeaderStart + dataHeaderLength;
    _dataHeader = _dataBuffer.view(dataHeaderStart, rawDataStart);

    // Useful for uncompressed data.
    _rawData = _dataBuffer.view(rawDataStart, _dataBuffer.size());
  }

  @Override
  public void close() {
    // Nothing to close here.
  }

  /**
   * Helper method to get the chunk for a given row.
   * <ul>
   *   <li> If the chunk already exists in the reader context, returns the same. </li>
   *   <li> Otherwise, loads the chunk for the row, and sets it in the reader context. </li>
   * </ul>
   * @param row Row for which to get the chunk
   * @param context Reader context
   * @return Chunk for the row
   */
  protected ByteBuffer getChunkForRow(int row, ChunkReaderContext context) {
    int chunkId = row / _numDocsPerChunk;
    if (context.getChunkId() == chunkId) {
      return context.getChunkBuffer();
    }

    int chunkSize;
    int chunkPosition = getChunkPosition(chunkId);

    // Size of chunk can be determined using next chunks offset, or end of data buffer for last chunk.
    if (chunkId == (_numChunks - 1)) { // Last chunk.
      chunkSize = (int) (_dataBuffer.size() - chunkPosition);
    } else {
      int nextChunkOffset = getChunkPosition(chunkId + 1);
      chunkSize = nextChunkOffset - chunkPosition;
    }

    ByteBuffer decompressedBuffer = context.getChunkBuffer();
    decompressedBuffer.clear();

    try {
      _chunkDecompressor.decompress(_dataBuffer.toDirectByteBuffer(chunkPosition, chunkSize), decompressedBuffer);
    } catch (IOException e) {
      LOGGER.error("Exception caught while decompressing data chunk", e);
      throw new RuntimeException(e);
    }
    context.setChunkId(chunkId);
    return decompressedBuffer;
  }

  /**
   * Helper method to get the offset of the chunk in the data.
   *
   * @param chunkId Id of the chunk for which to return the position.
   * @return Position (offset) of the chunk in the data.
   */
  protected int getChunkPosition(int chunkId) {
    return _dataHeader.getInt(chunkId * INT_SIZE);
  }

  /**
   * Method to determine if the data is compressed or not.
   *
   * @return True if data is compressed, false otherwise.
   */
  protected boolean isCompressed() {
    return (_isCompressed);
  }

  /**
   * Returns a PinotDataBuffer containing the raw data.
   *
   * @return PinotDataBuffer containing raw data.
   */
  protected PinotDataBuffer getRawData() {
    return _rawData;
  }
}
