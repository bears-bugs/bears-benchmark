package com.linkedin.thirdeye.anomalydetection.function;

import com.linkedin.pinot.pql.parsers.utils.Pair;
import com.linkedin.thirdeye.anomaly.views.AnomalyTimelinesView;
import com.linkedin.thirdeye.anomalydetection.context.AnomalyDetectionContext;
import com.linkedin.thirdeye.anomalydetection.context.TimeSeries;
import com.linkedin.thirdeye.anomalydetection.model.data.DataModel;
import com.linkedin.thirdeye.anomalydetection.model.data.NoopDataModel;
import com.linkedin.thirdeye.anomalydetection.model.detection.DetectionModel;
import com.linkedin.thirdeye.anomalydetection.model.detection.NoopDetectionModel;
import com.linkedin.thirdeye.anomalydetection.model.merge.MergeModel;
import com.linkedin.thirdeye.anomalydetection.model.merge.NoPredictionMergeModel;
import com.linkedin.thirdeye.anomalydetection.model.merge.NoopMergeModel;
import com.linkedin.thirdeye.anomalydetection.model.prediction.ExpectedTimeSeriesPredictionModel;
import com.linkedin.thirdeye.anomalydetection.model.prediction.NoopPredictionModel;
import com.linkedin.thirdeye.anomalydetection.model.prediction.PredictionModel;
import com.linkedin.thirdeye.anomalydetection.model.transform.TransformationFunction;
import com.linkedin.thirdeye.api.DimensionMap;
import com.linkedin.thirdeye.api.MetricTimeSeries;
import com.linkedin.thirdeye.dashboard.views.TimeBucket;
import com.linkedin.thirdeye.datalayer.dto.AnomalyFunctionDTO;
import com.linkedin.thirdeye.datalayer.dto.MergedAnomalyResultDTO;
import com.linkedin.thirdeye.datalayer.dto.RawAnomalyResultDTO;
import com.linkedin.thirdeye.detector.function.BaseAnomalyFunction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class provides the default control logic to perform actions on an anomaly detection context
 * with the given anomaly detection module; the actions can be anomaly detection, information update
 * of merged anomalies, etc.
 * Note that this class provides the default anomaly detection flow for only one single metric (main
 * metric), the other metrics that come along in AnomalyDetectionContext are used as auxiliary
 * metrics, e.g., they provide additional information during the transformation or detection of the
 * main metric.
 */
public abstract class AbstractModularizedAnomalyFunction extends BaseAnomalyFunction
    implements AnomalyDetectionFunction, ModularizedAnomalyFunctionModelProvider {
  protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  protected DataModel dataModel = new NoopDataModel();
  protected List<TransformationFunction> currentTimeSeriesTransformationChain = new ArrayList<>();
  protected List<TransformationFunction> baselineTimeSeriesTransformationChain = new ArrayList<>();
  protected PredictionModel predictionModel = new NoopPredictionModel();
  protected DetectionModel detectionModel = new NoopDetectionModel();
  protected MergeModel mergeModel = new NoopMergeModel();

  protected AnomalyFunctionDTO spec;
  protected Properties properties;

  @Override
  public void init(AnomalyFunctionDTO spec) throws Exception {
    this.spec = spec;
    super.init(spec);
    this.properties = spec.toProperties();
  }

  @Override
  public AnomalyFunctionDTO getSpec() {
    return spec;
  }

  @Override
  public List<Interval> getTimeSeriesIntervals(long monitoringWindowStartTime,
      long monitoringWindowEndTime) {
    return getDataModel().getAllDataIntervals(monitoringWindowStartTime, monitoringWindowEndTime);
  }

  @Override
  public List<RawAnomalyResultDTO> analyze(AnomalyDetectionContext anomalyDetectionContext)
      throws Exception {
    if (!checkPrecondition(anomalyDetectionContext)) {
      LOGGER.error("The precondition of anomaly detection context does not hold: please make sure"
          + "the observed time series and anomaly function are not null.");
      return Collections.emptyList();
    }

    String mainMetric = anomalyDetectionContext.getAnomalyDetectionFunction().getSpec().getTopicMetric();

    // Transform current and baseline time series and train the prediction model
    transformAndPredictTimeSeries(mainMetric, anomalyDetectionContext);

    // Detect anomalies
    return getDetectionModel().detect(mainMetric, anomalyDetectionContext);
  }

  @Override
  public void updateMergedAnomalyInfo(AnomalyDetectionContext anomalyDetectionContext,
      MergedAnomalyResultDTO anomalyToUpdated) throws Exception {
    MergeModel mergeModel = getMergeModel();
    if (!(mergeModel instanceof NoPredictionMergeModel)) {
      if (checkPrecondition(anomalyDetectionContext)) {
        String mainMetric = anomalyDetectionContext.getAnomalyDetectionFunction().getSpec().getTopicMetric();
        // Transform current and baseline time series and train the prediction model
        transformAndPredictTimeSeries(mainMetric, anomalyDetectionContext);
      } else {
        LOGGER.error("The precondition of anomaly detection context does not hold: please make sure"
            + "the observed time series and anomaly function are not null.");
        return;
      }
    }
    mergeModel.update(anomalyDetectionContext, anomalyToUpdated);
  }

  @Override
  public AnomalyTimelinesView getTimeSeriesView(AnomalyDetectionContext anomalyDetectionContext, long bucketMillis,
      String metric, long viewWindowStartTime, long viewWindowEndTime, List<MergedAnomalyResultDTO> knownAnomalies) {
    String mainMetric = anomalyDetectionContext.getAnomalyDetectionFunction().getSpec().getTopicMetric();

    this.transformAndPredictTimeSeries(mainMetric, anomalyDetectionContext);

    // Construct AnomalyTimelinesView
    AnomalyTimelinesView anomalyTimelinesView = new AnomalyTimelinesView();

    // Add current (observed) time series
    TimeSeries observedTS = anomalyDetectionContext.getTransformedCurrent(mainMetric);
    int bucketCount = (int) ((viewWindowEndTime - viewWindowStartTime) / bucketMillis);
    for (int i = 0; i < bucketCount; ++i) {
      long currentBucketMillis = viewWindowStartTime + i * bucketMillis;
      double observedValue = 0d;
      if (observedTS.hasTimestamp(currentBucketMillis)) {
        observedValue = observedTS.get(currentBucketMillis);
      }
      TimeBucket timebucket =
          new TimeBucket(currentBucketMillis, currentBucketMillis + bucketMillis, currentBucketMillis,
              currentBucketMillis + bucketMillis);
      anomalyTimelinesView.addTimeBuckets(timebucket);
      anomalyTimelinesView.addCurrentValues(observedValue);
    }

    // Add baseline time series
    PredictionModel trainedPredictionModel = anomalyDetectionContext.getTrainedPredictionModel(mainMetric);
    if (trainedPredictionModel instanceof ExpectedTimeSeriesPredictionModel) {
      TimeSeries expectedTS = ((ExpectedTimeSeriesPredictionModel) trainedPredictionModel).getExpectedTimeSeries();
      long expectedTSStartTime = expectedTS.getTimeSeriesInterval().getStartMillis();
      for (int i = 0; i < bucketCount; ++i) {
        long baselineBucketMillis = expectedTSStartTime + i * bucketMillis;
        double expectedValue = 0d;
        if (expectedTS.hasTimestamp(baselineBucketMillis)) {
          expectedValue = expectedTS.get(baselineBucketMillis);
        }
        anomalyTimelinesView.addBaselineValues(expectedValue);
      }
    }

    return anomalyTimelinesView;
  }

  /**
   * Returns true if the following conditions hold:
   * 1. Anomaly detection context is not null.
   * 2. Anomaly detection function is not null.
   * 3. Current time series is not null.
   *
   * Note: Baseline time series could be null for MIN_MAX_FUNCTION.
   *
   * @param anomalyDetectionContext the context for anomaly detection.
   * @return true if the context satisfies the pre-condition of anomaly detection.
   */
  protected static boolean checkPrecondition(AnomalyDetectionContext anomalyDetectionContext) {
    return anomalyDetectionContext != null && anomalyDetectionContext.getAnomalyDetectionFunction() != null;
  }

  /**
   * Performs the following operations on the given an anomaly detection context.
   * 1. Transform current and baseline time series.
   * 2. Train prediction model using the baseline time series.
   *
   * At the end of this method, a transformed current time series, transformed baselines, and a
   * trained prediction model are appended to the given anomaly detection context.
   *
   * The processed anomaly detection context has multiple usages. For example, it could be used for
   * detecting anomalies, plotting UI, updating the information of anomalies, etc.
   *
   * @param metricName the name of the metric on which we apply transformation and prediction
   * @param anomalyDetectionContext anomaly detection context that contains the necessary time
   *                                series for preparing the prediction model
   */
  public void transformAndPredictTimeSeries(String metricName, AnomalyDetectionContext anomalyDetectionContext) {
    transformTimeSeries(metricName, anomalyDetectionContext);

    // Train Prediction Model
    PredictionModel predictionModel = getPredictionModel();
    predictionModel.train(anomalyDetectionContext.getTransformedBaselines(metricName), anomalyDetectionContext);
    anomalyDetectionContext.setTrainedPredictionModel(metricName, predictionModel);
  }

  /**
   * Transform the current time series and baselines.
   *
   * TODO: Apply Chain-of-Responsibility on the transformation chain
   *
   * @param metricName the name of the metric on which we apply transformation and prediction
   * @param anomalyDetectionContext anomaly detection context that contains the time series to be
   *                                transformed.
   */
  private void transformTimeSeries(String metricName, AnomalyDetectionContext anomalyDetectionContext) {
    // Transform the observed (current) time series
    if (anomalyDetectionContext.getTransformedCurrent(metricName) == null) {
      anomalyDetectionContext.setTransformedCurrent(metricName, anomalyDetectionContext.getCurrent(metricName));
    }
    List<TransformationFunction> currentTimeSeriesTransformationChain =
        getCurrentTimeSeriesTransformationChain();
    if (CollectionUtils.isNotEmpty(currentTimeSeriesTransformationChain)) {
      for (TransformationFunction tf : currentTimeSeriesTransformationChain) {
        anomalyDetectionContext
            .setTransformedCurrent(metricName, tf.transform(anomalyDetectionContext.getTransformedCurrent(metricName),
                anomalyDetectionContext));
      }
    }

    // Transform baseline time series
    if (anomalyDetectionContext.getTransformedBaselines(metricName) == null) {
      anomalyDetectionContext.setTransformedBaselines(metricName, anomalyDetectionContext.getBaselines(metricName));
    }
    List<TransformationFunction> baselineTimeSeriesTransformationChain =
        getBaselineTimeSeriesTransformationChain();
    if (CollectionUtils.isNotEmpty(anomalyDetectionContext.getTransformedBaselines(metricName))
        && CollectionUtils.isNotEmpty(baselineTimeSeriesTransformationChain)) {
      for (TransformationFunction tf : baselineTimeSeriesTransformationChain) {
        List<TimeSeries> transformedBaselines = new ArrayList<>();
        for (TimeSeries ts : anomalyDetectionContext.getTransformedBaselines(metricName)) {
          TimeSeries transformedTS = tf.transform(ts, anomalyDetectionContext);
          transformedBaselines.add(transformedTS);
        }
        anomalyDetectionContext.setTransformedBaselines(metricName, transformedBaselines);
      }
    }
  }

  //////////////////// Wrapper methods for backward compatibility /////////////////////////
  @Override
  public List<Pair<Long, Long>> getDataRangeIntervals(Long monitoringWindowStartTime,
      Long monitoringWindowEndTime) {
    List<Interval> timeSeriesIntervals =
        this.getTimeSeriesIntervals(monitoringWindowStartTime, monitoringWindowEndTime);
    return BackwardAnomalyFunctionUtils.toBackwardCompatibleDataRanges(timeSeriesIntervals);
  }

  @Override
  public List<RawAnomalyResultDTO> analyze(DimensionMap exploredDimensions, MetricTimeSeries timeSeries,
      DateTime windowStart, DateTime windowEnd, List<MergedAnomalyResultDTO> knownAnomalies)
      throws Exception {
    AnomalyDetectionContext anomalyDetectionContext = BackwardAnomalyFunctionUtils
        .buildAnomalyDetectionContext(this, timeSeries, spec.getTopicMetric(), exploredDimensions,
            spec.getBucketSize(), spec.getBucketUnit(), windowStart, windowEnd, knownAnomalies);

    return this.analyze(anomalyDetectionContext);
  }

  @Override
  public void updateMergedAnomalyInfo(MergedAnomalyResultDTO anomalyToUpdated,
      MetricTimeSeries timeSeries, DateTime windowStart, DateTime windowEnd,
      List<MergedAnomalyResultDTO> knownAnomalies) throws Exception {
    AnomalyDetectionContext anomalyDetectionContext = null;

    if (!(getMergeModel() instanceof NoPredictionMergeModel)) {
      anomalyDetectionContext = BackwardAnomalyFunctionUtils
          .buildAnomalyDetectionContext(this, timeSeries, spec.getTopicMetric(),
              anomalyToUpdated.getDimensions(), spec.getBucketSize(), spec.getBucketUnit(),
              windowStart, windowEnd, knownAnomalies);
    }

    updateMergedAnomalyInfo(anomalyDetectionContext, anomalyToUpdated);
  }

  // TODO: Generate time series view using ViewModel
  @Override
  public AnomalyTimelinesView getTimeSeriesView(MetricTimeSeries timeSeries,
      long bucketMillis, String metric, long viewWindowStartTime, long viewWindowEndTime,
      List<MergedAnomalyResultDTO> knownAnomalies) {
    AnomalyDetectionContext anomalyDetectionContext = BackwardAnomalyFunctionUtils
        .buildAnomalyDetectionContext(this, timeSeries, spec.getTopicMetric(), null,
            spec.getBucketSize(), spec.getBucketUnit(), new DateTime(viewWindowStartTime),
            new DateTime(viewWindowEndTime), knownAnomalies);

    return this.getTimeSeriesView(anomalyDetectionContext, bucketMillis, metric, viewWindowStartTime, viewWindowEndTime,
        knownAnomalies);
  }

  /**
   * Get the data model that defines the time ranges of the input time series
   * @return a data model of the anomaly function
   */
  @Override
  public DataModel getDataModel() {
    return this.dataModel;
  }

  /**
   * Get the transformation models to transform the current/monitoring time series
   * @return a list of transformation models
   */
  @Override
  public List<TransformationFunction> getCurrentTimeSeriesTransformationChain() {
    return this.currentTimeSeriesTransformationChain;
  }

  /**
   * Get the transformation models to transform the baseline time series
   * @return a list of transformation models
   */
  @Override
  public List<TransformationFunction> getBaselineTimeSeriesTransformationChain() {
    return this.baselineTimeSeriesTransformationChain;
  }

  /**
   * Get the prediction model of the anomaly function
   * @return the prediction model of the anomaly function
   */
  @Override
  public PredictionModel getPredictionModel() {
    return this.predictionModel;
  }

  /**
   * Get the detection model of the anomaly function
   * @return the detection model of the anomaly function
   */
  @Override
  public DetectionModel getDetectionModel() {
    return this.detectionModel;
  }

  /**
   * Get the merge model of the anomaly function
   * @return the merge model of the anomaly function
   */
  @Override
  public MergeModel getMergeModel() {
    return this.mergeModel;
  }

  /**
   * Determine if the history anomalies are used in the anomaly function
   * TODO: Remove this flag after the migration to modularized functions is done. The pipeline itself has the ability to determine if the history anomalies should be used.
   * @return true if the history anomalies should be included in the context
   */
  @Override
  public boolean useHistoryAnomaly() {
    return true;
  }
}
