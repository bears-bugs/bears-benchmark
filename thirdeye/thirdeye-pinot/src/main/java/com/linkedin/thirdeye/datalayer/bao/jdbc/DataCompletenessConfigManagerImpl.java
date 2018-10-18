package com.linkedin.thirdeye.datalayer.bao.jdbc;

import com.google.inject.Singleton;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

import com.linkedin.thirdeye.datalayer.bao.DataCompletenessConfigManager;
import com.linkedin.thirdeye.datalayer.dto.DataCompletenessConfigDTO;
import com.linkedin.thirdeye.datalayer.pojo.DataCompletenessConfigBean;
import com.linkedin.thirdeye.datalayer.util.Predicate;

@Singleton
public class DataCompletenessConfigManagerImpl extends AbstractManagerImpl<DataCompletenessConfigDTO> implements DataCompletenessConfigManager {


  public DataCompletenessConfigManagerImpl() {
    super(DataCompletenessConfigDTO.class, DataCompletenessConfigBean.class);
  }

  @Override
  public List<DataCompletenessConfigDTO> findAllByDataset(String dataset) {
    Predicate predicate = Predicate.EQ("dataset", dataset);
    return findByPredicate(predicate);
  }

  @Override
  public DataCompletenessConfigDTO findByDatasetAndDateSDF(String dataset, String dateToCheckInSDF) {
    Predicate predicate =
        Predicate.AND(Predicate.EQ("dataset", dataset), Predicate.EQ("dateToCheckInSDF", dateToCheckInSDF));

    List<DataCompletenessConfigBean> list = genericPojoDao.get(predicate, DataCompletenessConfigBean.class);
    DataCompletenessConfigDTO result = null;
    if (CollectionUtils.isNotEmpty(list)) {
      result = MODEL_MAPPER.map(list.get(0), DataCompletenessConfigDTO.class);
    }
    return result;
  }


  @Override
  public DataCompletenessConfigDTO findByDatasetAndDateMS(String dataset, Long dateToCheckInMS) {
    Predicate predicate =
        Predicate.AND(Predicate.EQ("dataset", dataset), Predicate.EQ("dateToCheckInMS", dateToCheckInMS));

    List<DataCompletenessConfigBean> list = genericPojoDao.get(predicate, DataCompletenessConfigBean.class);
    DataCompletenessConfigDTO result = null;
    if (CollectionUtils.isNotEmpty(list)) {
      result = MODEL_MAPPER.map(list.get(0), DataCompletenessConfigDTO.class);
    }
    return result;
  }

  @Override
  public List<DataCompletenessConfigDTO> findAllInTimeRange(long startTime, long endTime) {
    Predicate timePredicate = Predicate.AND(Predicate.GE("dateToCheckInMS", startTime), Predicate.LT("dateToCheckInMS", endTime));
    return findByPredicate(timePredicate);
  }

  @Override
  public List<DataCompletenessConfigDTO> findAllByDatasetAndInTimeRange(String dataset, long startTime, long endTime) {
    Predicate timePredicate = Predicate.AND(Predicate.GE("dateToCheckInMS", startTime), Predicate.LT("dateToCheckInMS", endTime));
    Predicate datasetPredicate = Predicate.EQ("dataset", dataset);
    Predicate predicate = Predicate.AND(datasetPredicate, timePredicate);
    return findByPredicate(predicate);
  }

  @Override
  public List<DataCompletenessConfigDTO> findAllByDatasetAndInTimeRangeAndPercentCompleteGT(String dataset,
      long startTime, long endTime, double percentComplete) {
    Predicate timePredicate = Predicate.AND(Predicate.GE("dateToCheckInMS", startTime), Predicate.LT("dateToCheckInMS", endTime));
    Predicate datasetPredicate = Predicate.EQ("dataset", dataset);
    Predicate percentCompletePrediate = Predicate.GT("percentComplete", percentComplete);
    Predicate predicate = Predicate.AND(datasetPredicate, timePredicate, percentCompletePrediate);
    return findByPredicate(predicate);
  }

  @Override
  public List<DataCompletenessConfigDTO> findAllByTimeOlderThan(long time) {
    Predicate predicate = Predicate.LT("dateToCheckInMS", time);
    return findByPredicate(predicate);
  }

  @Override
  public List<DataCompletenessConfigDTO> findAllByTimeOlderThanAndStatus(long time, boolean dataComplete) {
    Predicate datePredicate = Predicate.LT("dateToCheckInMS", time);
    Predicate dataCompletePredicate = Predicate.EQ("dataComplete", dataComplete);
    Predicate predicate = Predicate.AND(datePredicate, dataCompletePredicate);
    return findByPredicate(predicate);
  }

  @Override
  public List<DataCompletenessConfigDTO> findAllByDatasetAndInTimeRangeAndStatus(String dataset, long startTime,
      long endTime, boolean dataComplete) {
    Predicate timePredicate = Predicate.AND(Predicate.GE("dateToCheckInMS", startTime), Predicate.LT("dateToCheckInMS", endTime));
    Predicate datasetPredicate = Predicate.EQ("dataset", dataset);
    Predicate statusPredicate = Predicate.EQ("dataComplete", dataComplete);
    Predicate finalPredicate = Predicate.AND(datasetPredicate, timePredicate, statusPredicate);
    return findByPredicate(finalPredicate);
  }
}
