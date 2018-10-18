package com.linkedin.thirdeye.datasource.pinot.resultset;

import com.google.common.collect.ImmutableList;
import com.linkedin.pinot.client.ResultSet;
import com.linkedin.pinot.client.ResultSetGroup;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The ThirdEye's own {@link ResultSetGroup} for storing multiple {@link ThirdEyeResultSet} (i.e., an equivalent class
 * to Pinot's {@link ResultSet}).
 */
public class ThirdEyeResultSetGroup {
  private ImmutableList<ThirdEyeResultSet> resultSets = ImmutableList.of();

  public ThirdEyeResultSetGroup() { }

  public ThirdEyeResultSetGroup(List<ThirdEyeResultSet> resultSets) {
    this.setResultSets(resultSets);
  }

  public int size() {
    return resultSets.size();
  }

  public ThirdEyeResultSet get(int idx) {
    return resultSets.get(idx);
  }

  public void setResultSets(List<ThirdEyeResultSet> resultSets) {
    if (CollectionUtils.isNotEmpty(resultSets)) {
      this.resultSets = ImmutableList.copyOf(resultSets);
    } else {
      this.resultSets = ImmutableList.of();
    }
  }

  public List<ThirdEyeResultSet> getResultSets() {
    return resultSets;
  }

  /**
   * Constructs a ThirdEyeResultSetGroup from Pinot's {@link ResultSetGroup}.
   *
   * @param resultSetGroup a {@link ResultSetGroup} from Pinot.
   *
   * @return a converted {@link ThirdEyeResultSetGroup}.
   */
  public static ThirdEyeResultSetGroup fromPinotResultSetGroup(ResultSetGroup resultSetGroup) {
    List<ResultSet> resultSets = new ArrayList<>();
    for (int i = 0; i < resultSetGroup.getResultSetCount(); i++) {
      resultSets.add(resultSetGroup.getResultSet(i));
    }
    // Convert Pinot's ResultSet to ThirdEyeResultSet
    List<ThirdEyeResultSet> thirdEyeResultSets = new ArrayList<>();
    for (ResultSet resultSet : resultSets) {
      ThirdEyeResultSet thirdEyeResultSet = ThirdEyeDataFrameResultSet.fromPinotResultSet(resultSet);
      thirdEyeResultSets.add(thirdEyeResultSet);
    }

    return new ThirdEyeResultSetGroup(thirdEyeResultSets);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("resultSets", resultSets).toString();
  }
}
