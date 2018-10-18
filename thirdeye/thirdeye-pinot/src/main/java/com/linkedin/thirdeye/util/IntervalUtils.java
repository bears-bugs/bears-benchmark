package com.linkedin.thirdeye.util;

import com.linkedin.thirdeye.api.DimensionMap;
import com.linkedin.thirdeye.datalayer.dto.MergedAnomalyResultDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.joda.time.Interval;


public class IntervalUtils {
  /**
   * This method is designed to merge a list of intervals to a list of intervals with no overlap in between
   * @param intervals
   * @return
   * a list of intervals with no overlap in between
   */
  public static List<Interval> mergeIntervals (List<Interval> intervals) {
    if(intervals == null || intervals.size() == 0) {
      return intervals;
    }
    // Sort Intervals
    Collections.sort(intervals, new Comparator<Interval>() {
      @Override
      public int compare(Interval o1, Interval o2) {
        return o1.getStart().compareTo(o2.getStart());
      }
    });

    // Merge intervals
    Stack<Interval> intervalStack = new Stack<>();
    intervalStack.push(intervals.get(0));

    for(int i = 1; i < intervals.size(); i++) {
      Interval top = intervalStack.peek();
      Interval target = intervals.get(i);

      if(top.overlap(target) == null && (top.getEnd() != target.getStart())) {
        intervalStack.push(target);
      }
      else if(top.equals(target)) {
        continue;
      }
      else {
        Interval newTop = new Interval(Math.min(top.getStart().getMillis(), target.getStart().getMillis()),
            Math.max(top.getEnd().getMillis(), target.getEnd().getMillis()));
        intervalStack.pop();
        intervalStack.push(newTop);
      }
    }

    return intervalStack;
  }

  /**
   * Merge intervals for each dimension map
   * @param anomalyIntervals
   */
  public static void mergeIntervals(Map<DimensionMap, List<Interval>> anomalyIntervals) {
    for(DimensionMap dimension : anomalyIntervals.keySet()) {
      anomalyIntervals.put(dimension ,mergeIntervals(anomalyIntervals.get(dimension)));
    }
  }
}
