package com.linkedin.thirdeye.datasource.pinot.resultset;

import com.google.common.base.Preconditions;
import com.linkedin.pinot.client.ResultSet;
import com.linkedin.thirdeye.dataframe.DataFrame;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * An unified container that store Select, Aggregation, and Group-By {@link ResultSet} in a data frame.
 */
public class ThirdEyeDataFrameResultSet extends AbstractThirdEyeResultSet {
  private ThirdEyeResultSetMetaData thirdEyeResultSetMetaData;
  private DataFrame dataFrame;

  public ThirdEyeDataFrameResultSet(ThirdEyeResultSetMetaData thirdEyeResultSetMetaData, DataFrame dataFrame) {
    Preconditions.checkState(isMetaDataAndDataHaveSameColumns(thirdEyeResultSetMetaData, dataFrame),
        "Meta data and data's columns do not match.");

    this.thirdEyeResultSetMetaData = thirdEyeResultSetMetaData;
    this.dataFrame = dataFrame;
  }

  private boolean isMetaDataAndDataHaveSameColumns(ThirdEyeResultSetMetaData thirdEyeResultSetMetaData, DataFrame dataFrame) {
    Set<String> metaDataAllColumns = new HashSet<>(thirdEyeResultSetMetaData.getAllColumnNames());
    return metaDataAllColumns.equals(dataFrame.getSeries().keySet());
  }

  @Override
  public int getRowCount() {
    return dataFrame.size();
  }

  @Override
  public int getColumnCount() {
    return thirdEyeResultSetMetaData.getMetricColumnNames().size();
  }

  @Override
  public String getColumnName(int columnIdx) {
    Preconditions.checkPositionIndexes(0, columnIdx, thirdEyeResultSetMetaData.getMetricColumnNames().size() - 1);
    return thirdEyeResultSetMetaData.getMetricColumnNames().get(columnIdx);
  }

  @Override
  public String getString(int rowIdx, int columnIdx) {
    Preconditions.checkPositionIndexes(0, columnIdx, thirdEyeResultSetMetaData.getMetricColumnNames().size() - 1);
    return dataFrame.get(thirdEyeResultSetMetaData.getMetricColumnNames().get(columnIdx)).getString(rowIdx);
  }

  @Override
  public int getGroupKeyLength() {
    return thirdEyeResultSetMetaData.getGroupKeyColumnNames().size();
  }

  @Override
  public String getGroupKeyColumnName(int columnIdx) {
    Preconditions.checkPositionIndexes(0, columnIdx, getGroupKeyLength() - 1);
    return thirdEyeResultSetMetaData.getGroupKeyColumnNames().get(columnIdx);
  }

  @Override
  public String getGroupKeyColumnValue(int rowIdx, int columnIdx) {
    Preconditions.checkPositionIndexes(0, columnIdx, getGroupKeyLength() - 1);
    return dataFrame.get(thirdEyeResultSetMetaData.getGroupKeyColumnNames().get(columnIdx)).getString(rowIdx);
  }

  /**
   * Constructs a {@link ThirdEyeDataFrameResultSet} from any Pinot's {@link ResultSet}.
   *
   * @param resultSet A result set from Pinot.
   *
   * @return an unified {@link ThirdEyeDataFrameResultSet}.
   */
  public static ThirdEyeDataFrameResultSet fromPinotResultSet(ResultSet resultSet) {
    // Build the meta data of this result set
    List<String> groupKeyColumnNames = new ArrayList<>();
    int groupByColumnCount = 0;
    try {
      groupByColumnCount = resultSet.getGroupKeyLength();
    } catch (Exception e) {
      // Only happens when result set is GroupByResultSet type and contains empty result.
      // In this case, we have to use brutal force to count the number of group by columns.
      while (true) {
        try {
          resultSet.getGroupKeyColumnName(groupByColumnCount);
          ++groupByColumnCount;
        } catch (Exception breakSignal) {
          break;
        }
      }
    }
    for (int groupKeyColumnIdx = 0; groupKeyColumnIdx < groupByColumnCount; groupKeyColumnIdx++) {
      groupKeyColumnNames.add(resultSet.getGroupKeyColumnName(groupKeyColumnIdx));
    }
    List<String> metricColumnNames = new ArrayList<>();
    for (int columnIdx = 0; columnIdx < resultSet.getColumnCount(); columnIdx++) {
      metricColumnNames.add(resultSet.getColumnName(columnIdx));
    }
    ThirdEyeResultSetMetaData thirdEyeResultSetMetaData =
        new ThirdEyeResultSetMetaData(groupKeyColumnNames, metricColumnNames);

    // Build the DataFrame
    List<String> columnNameWithDataType = new ArrayList<>();
    //   Always cast dimension values to STRING type
    for (String groupColumnName : thirdEyeResultSetMetaData.getGroupKeyColumnNames()) {
      columnNameWithDataType.add(groupColumnName + ":STRING");
    }
    columnNameWithDataType.addAll(thirdEyeResultSetMetaData.getMetricColumnNames());
    DataFrame.Builder dfBuilder = DataFrame.builder(columnNameWithDataType);
    int rowCount = resultSet.getRowCount();
    int metricColumnCount = resultSet.getColumnCount();
    int totalColumnCount = groupByColumnCount + metricColumnCount;
    // Dump the values in ResultSet to the DataFrame
    for (int rowIdx = 0; rowIdx < rowCount; rowIdx++) {
      String[] columnsOfTheRow = new String[totalColumnCount];
      // GroupBy column value(i.e., dimension values)
      for (int groupByColumnIdx = 0; groupByColumnIdx < groupByColumnCount; groupByColumnIdx++) {
        String valueString = null;
        try {
          valueString = resultSet.getGroupKeyString(rowIdx, groupByColumnIdx);
        } catch (Exception e) {
          // Do nothing and subsequently insert a null value to the current series.
        }
        columnsOfTheRow[groupByColumnIdx] = valueString;
      }
      // Metric column's value
      for (int metricColumnIdx = 0; metricColumnIdx < metricColumnCount; metricColumnIdx++) {
        String valueString = null;
        try {
          valueString = resultSet.getString(rowIdx, metricColumnIdx);
        } catch (Exception e) {
          // Do nothing and subsequently insert a null value to the current series.
        }
        columnsOfTheRow[metricColumnIdx + groupByColumnCount] = valueString;
      }
      dfBuilder.append(columnsOfTheRow);
    }
    DataFrame dataFrame = dfBuilder.build();
    // Build ThirdEye's result set
    ThirdEyeDataFrameResultSet thirdEyeDataFrameResultSet =
        new ThirdEyeDataFrameResultSet(thirdEyeResultSetMetaData, dataFrame);

    return thirdEyeDataFrameResultSet;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ThirdEyeDataFrameResultSet that = (ThirdEyeDataFrameResultSet) o;
    return Objects.equals(thirdEyeResultSetMetaData, that.thirdEyeResultSetMetaData) && Objects.equals(dataFrame,
        that.dataFrame);
  }

  @Override
  public int hashCode() {
    return Objects.hash(thirdEyeResultSetMetaData, dataFrame);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ThirdEyeDataFrameResultSet{");
    sb.append("metaData=").append(thirdEyeResultSetMetaData);
    sb.append(", data=").append(dataFrame);
    sb.append('}');
    return sb.toString();
  }
}
