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
package com.linkedin.pinot.core.segment.creator.impl;

import com.linkedin.pinot.common.data.FieldSpec;
import com.linkedin.pinot.common.data.MetricFieldSpec;
import com.linkedin.pinot.core.io.writer.impl.FixedByteSingleValueMultiColWriter;
import it.unimi.dsi.fastutil.doubles.Double2IntOpenHashMap;
import it.unimi.dsi.fastutil.floats.Float2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SegmentDictionaryCreator implements Closeable {
  private static final Logger LOGGER = LoggerFactory.getLogger(SegmentDictionaryCreator.class);
  private static final Charset UTF_8 = Charset.forName("UTF-8");

  private final Object sortedList;
  private final FieldSpec spec;
  private final File dictionaryFile;
  private final int rowCount;

  private Int2IntOpenHashMap intValueToIndexMap;
  private Long2IntOpenHashMap longValueToIndexMap;
  private Float2IntOpenHashMap floatValueToIndexMap;
  private Double2IntOpenHashMap doubleValueToIndexMap;
  private Object2IntOpenHashMap<String> stringValueToIndexMap;

  private int stringColumnMaxLength = 0;

  public SegmentDictionaryCreator(boolean hasNulls, Object sortedList, FieldSpec spec, File indexDir)
      throws IOException {
    rowCount = ArrayUtils.getLength(sortedList);

    Object first = null;
    Object last = null;

    if (0 < rowCount) {
      if (sortedList instanceof int[]) {
        int[] intSortedList = (int[]) sortedList;
        first = intSortedList[0];
        last = intSortedList[rowCount - 1];
      } else if (sortedList instanceof long[]) {
        long[] longSortedList = (long[]) sortedList;
        first = longSortedList[0];
        last = longSortedList[rowCount - 1];
      } else if (sortedList instanceof float[]) {
        float[] floatSortedList = (float[]) sortedList;
        first = floatSortedList[0];
        last = floatSortedList[rowCount - 1];
      } else if (sortedList instanceof double[]) {
        double[] doubleSortedList = (double[]) sortedList;
        first = doubleSortedList[0];
        last = doubleSortedList[rowCount - 1];
      } else if (sortedList instanceof String[]) {
        String[] intSortedList = (String[]) sortedList;
        first = intSortedList[0];
        last = intSortedList[rowCount - 1];
      } else if (sortedList instanceof Object[]) {
        Object[] intSortedList = (Object[]) sortedList;
        first = intSortedList[0];
        last = intSortedList[rowCount - 1];
      }
    }

    // make hll column log info different than other columns, since range makes no sense for hll column
    if (spec instanceof MetricFieldSpec &&
        ((MetricFieldSpec)spec).getDerivedMetricType() == MetricFieldSpec.DerivedMetricType.HLL) {
      LOGGER.info(
          "Creating segment for column {}, hasNulls = {}, cardinality = {}, dataType = {}, single value field = {}, is HLL derived column",
          spec.getName(), hasNulls, rowCount, spec.getDataType(), spec.isSingleValueField());
    } else {
      LOGGER.info(
          "Creating segment for column {}, hasNulls = {}, cardinality = {}, dataType = {}, single value field = {}, range = {} to {}",
          spec.getName(), hasNulls, rowCount, spec.getDataType(), spec.isSingleValueField(), first, last);
    }
    this.sortedList = sortedList;
    this.spec = spec;
    dictionaryFile = new File(indexDir, spec.getName() + V1Constants.Dict.FILE_EXTENSION);
    FileUtils.touch(dictionaryFile);
  }

  @Override
  public void close() throws IOException {
  }

  public void build() throws Exception {
    switch (spec.getDataType()) {
      case INT:
        final FixedByteSingleValueMultiColWriter intDictionaryWrite =
            new FixedByteSingleValueMultiColWriter(dictionaryFile, rowCount, 1,
                V1Constants.Dict.INT_DICTIONARY_COL_SIZE);
        intValueToIndexMap = new Int2IntOpenHashMap(rowCount);
        int[] sortedInts = (int[]) sortedList;
        for (int i = 0; i < rowCount; i++) {
          final int entry = sortedInts[i];
          intDictionaryWrite.setInt(i, 0, entry);
          intValueToIndexMap.put(entry, i);
        }
        intDictionaryWrite.close();
        break;
      case FLOAT:
        final FixedByteSingleValueMultiColWriter floatDictionaryWrite =
            new FixedByteSingleValueMultiColWriter(dictionaryFile, rowCount, 1,
                V1Constants.Dict.FLOAT_DICTIONARY_COL_SIZE);
        floatValueToIndexMap = new Float2IntOpenHashMap(rowCount);
        float[] sortedFloats = (float[]) sortedList;
        for (int i = 0; i < rowCount; i++) {
          final float entry = sortedFloats[i];
          floatDictionaryWrite.setFloat(i, 0, entry);
          floatValueToIndexMap.put(entry, i);
        }
        floatDictionaryWrite.close();
        break;
      case LONG:
        final FixedByteSingleValueMultiColWriter longDictionaryWrite =
            new FixedByteSingleValueMultiColWriter(dictionaryFile, rowCount, 1,
                V1Constants.Dict.LONG_DICTIONARY_COL_SIZE);
        longValueToIndexMap = new Long2IntOpenHashMap(rowCount);
        long[] sortedLongs = (long[]) sortedList;
        for (int i = 0; i < rowCount; i++) {
          final long entry = sortedLongs[i];
          longDictionaryWrite.setLong(i, 0, entry);
          longValueToIndexMap.put(entry, i);
        }
        longDictionaryWrite.close();
        break;
      case DOUBLE:
        final FixedByteSingleValueMultiColWriter doubleDictionaryWrite =
            new FixedByteSingleValueMultiColWriter(dictionaryFile, rowCount, 1,
                V1Constants.Dict.DOUBLE_DICTIONARY_COL_SIZE);
        doubleValueToIndexMap = new Double2IntOpenHashMap(rowCount);
        double[] sortedDoubles = (double[]) sortedList;
        for (int i = 0; i < rowCount; i++) {
          final double entry = sortedDoubles[i];
          doubleDictionaryWrite.setDouble(i, 0, entry);
          doubleValueToIndexMap.put(entry, i);
        }
        doubleDictionaryWrite.close();
        break;
      case STRING:
        // TODO: here we call getBytes() multiple times, could be optimized
        String[] sortedStrings = (String[]) sortedList;

        // Get the maximum length of all entries
        stringColumnMaxLength = 1; // make sure that there is non-zero sized dictionary JIRA:PINOT-2947
        for (String entry : sortedStrings) {
          int length = entry.getBytes(UTF_8).length;
          if (stringColumnMaxLength < length) {
            stringColumnMaxLength = length;
          }
        }

        final FixedByteSingleValueMultiColWriter stringDictionaryWrite =
            new FixedByteSingleValueMultiColWriter(dictionaryFile, rowCount, 1, new int[]{stringColumnMaxLength});
        stringValueToIndexMap = new Object2IntOpenHashMap<>(rowCount);
        for (int i = 0; i < rowCount; i++) {
          String entry = sortedStrings[i];
          stringDictionaryWrite.setString(i, 0, getPaddedString(entry, stringColumnMaxLength));
          stringValueToIndexMap.put(entry, i);
        }
        stringDictionaryWrite.close();
        break;
      default:
        throw new RuntimeException("Unhandled type " + spec.getDataType());
    }
  }

  public int getStringColumnMaxLength() {
    return stringColumnMaxLength;
  }

  public int indexOfSV(Object e) {
    switch (spec.getDataType()) {
      case INT:
        return intValueToIndexMap.get(e);
      case FLOAT:
        return floatValueToIndexMap.get(e);
      case DOUBLE:
        return doubleValueToIndexMap.get(e);
      case LONG:
        return longValueToIndexMap.get(e);
      case STRING:
      case BOOLEAN:
        String value = e.toString();
        return stringValueToIndexMap.get(value);
      default:
        throw new UnsupportedOperationException("Unsupported data type : " + spec.getDataType() +
            " for column : " + spec.getName());
    }
  }

  public int[] indexOfMV(Object e) {

    final Object[] multiValues = (Object[]) e;
    final int[] ret = new int[multiValues.length];

    switch (spec.getDataType()) {
      case INT:
        for (int i = 0; i < multiValues.length; i++) {
          ret[i] = intValueToIndexMap.get(multiValues[i]);
        }
        break;
      case FLOAT:
        for (int i = 0; i < multiValues.length; i++) {
          ret[i] = floatValueToIndexMap.get(multiValues[i]);
        }
        break;
      case LONG:
        for (int i = 0; i < multiValues.length; i++) {
          ret[i] = longValueToIndexMap.get(multiValues[i]);
        }
        break;
      case DOUBLE:
        for (int i = 0; i < multiValues.length; i++) {
          ret[i] = doubleValueToIndexMap.get(multiValues[i]);
        }
        break;
      case STRING:
      case BOOLEAN:
        for (int i = 0; i < multiValues.length; i++) {
          String value = multiValues[i].toString();
          ret[i] = stringValueToIndexMap.get(value);
        }
        break;
      default:
        throw new UnsupportedOperationException("Unsupported data type : " + spec.getDataType() +
            " for multivalue column : " + spec.getName());
    }

    return ret;
  }

  /**
   * Given an input string and a target length, appends padding characters (<code>'\0'</code>) to the string to make it
   * of desired length.
   */
  public static String getPaddedString(String inputString, int targetLength) {
    byte[] bytes = inputString.getBytes(UTF_8);
    if (bytes.length == targetLength) {
      return inputString;
    }

    StringBuilder stringBuilder = new StringBuilder(inputString);
    int numBytesToPad = targetLength - bytes.length;
    for (int i = 0; i < numBytesToPad; i++) {
      stringBuilder.append(V1Constants.Str.DEFAULT_STRING_PAD_CHAR);
    }
    return stringBuilder.toString();
  }
}
