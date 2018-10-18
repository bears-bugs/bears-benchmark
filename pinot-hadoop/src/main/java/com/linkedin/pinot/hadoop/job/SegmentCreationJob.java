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
package com.linkedin.pinot.hadoop.job;

import com.linkedin.pinot.common.Utils;
import com.linkedin.pinot.common.data.Schema;
import com.linkedin.pinot.hadoop.job.mapper.HadoopSegmentCreationMapReduceJob;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobContext;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SegmentCreationJob extends Configured {

  private static final String PATH_TO_DEPS_JAR = "path.to.deps.jar";
  private static final String TEMP = "temp";
  private static final String PATH_TO_SCHEMA = "path.to.schema";

  private static final Logger LOGGER = LoggerFactory.getLogger(SegmentCreationJob.class);

  private final String _jobName;
  private final Properties _properties;

  private final String _inputSegmentDir;
  private final String _stagingDir;
  private final Schema _dataSchema;
  private final String _depsJarPath;
  private final String _outputDir;

  public SegmentCreationJob(String jobName, Properties properties) throws Exception {
    super(new Configuration());
    getConf().set("mapreduce.job.user.classpath.first", "true");
    _jobName = jobName;
    _properties = properties;

    _inputSegmentDir = _properties.getProperty(JobConfigConstants.PATH_TO_INPUT);
    String schemaFilePath = _properties.getProperty(PATH_TO_SCHEMA);
    _outputDir = getOutputDir();
    _stagingDir = new File(_outputDir, TEMP).getAbsolutePath();
    _depsJarPath = _properties.getProperty(PATH_TO_DEPS_JAR, null);

    Utils.logVersions();

    LOGGER.info("*********************************************************************");
    LOGGER.info("path.to.input: {}", _inputSegmentDir);
    LOGGER.info("path.to.deps.jar: {}", _depsJarPath);
    LOGGER.info("path.to.output: {}", _outputDir);
    LOGGER.info("path.to.schema: {}", schemaFilePath);
    if (schemaFilePath != null) {
      _dataSchema = Schema.fromFile(new File(schemaFilePath));
    } else {
      _dataSchema = null;
    }
    LOGGER.info("schema: {}", _dataSchema);
    LOGGER.info("*********************************************************************");
  }

  protected String getOutputDir() {
    return _properties.getProperty(JobConfigConstants.PATH_TO_OUTPUT);
  }

  protected String getInputDir() {
    return _inputSegmentDir;
  }

  protected void setOutputPath(Configuration configuration) {

  }

  public void run() throws Exception {
    LOGGER.info("Starting {}", getClass().getSimpleName());

    FileSystem fs = FileSystem.get(getConf());
    Path inputPathPattern = new Path(_inputSegmentDir);

    if (fs.exists(new Path(_stagingDir))) {
      LOGGER.warn("Found the temp folder, deleting it");
      fs.delete(new Path(_stagingDir), true);
    }
    fs.mkdirs(new Path(_stagingDir));
    fs.mkdirs(new Path(_stagingDir + "/input/"));

    if (fs.exists(new Path(_outputDir))) {
      LOGGER.warn("Found the output folder {}, deleting it", _outputDir);
      fs.delete(new Path(_outputDir), true);
    }
    fs.mkdirs(new Path(_outputDir));

    List<FileStatus> inputDataFiles = new ArrayList<FileStatus>();
    FileStatus[] fileStatusArr = fs.globStatus(inputPathPattern);
    for (FileStatus fileStatus : fileStatusArr) {
      inputDataFiles.addAll(getDataFilesFromPath(fs, fileStatus.getPath()));
    }
    if (inputDataFiles.isEmpty()) {
      LOGGER.error("No Input paths {}", inputPathPattern);
      throw new RuntimeException("No input files " + inputPathPattern);
    }

    for (int seqId = 0; seqId < inputDataFiles.size(); ++seqId) {
      FileStatus file = inputDataFiles.get(seqId);
      String completeFilePath = " " + file.getPath().toString() + " " + seqId;
      Path newOutPutFile = new Path((_stagingDir + "/input/" +
          file.getPath().toString().replace('.', '_').replace('/', '_').replace(':', '_') + ".txt"));
      FSDataOutputStream stream = fs.create(newOutPutFile);
      stream.writeUTF(completeFilePath);
      stream.flush();
      stream.close();
    }

    Job job = Job.getInstance(getConf());

    setAdditionalJobProperties(job);

    job.setJarByClass(SegmentCreationJob.class);
    job.setJobName(_jobName);

    setMapperClass(job);

    if (System.getenv("HADOOP_TOKEN_FILE_LOCATION") != null) {
      job.getConfiguration().set("mapreduce.job.credentials.binary", System.getenv("HADOOP_TOKEN_FILE_LOCATION"));
    }

    job.setInputFormatClass(TextInputFormat.class);
    job.setOutputFormatClass(TextOutputFormat.class);

    job.setMapOutputKeyClass(LongWritable.class);
    job.setMapOutputValueClass(Text.class);

    FileInputFormat.addInputPath(job, new Path(_stagingDir + "/input/"));
    FileOutputFormat.setOutputPath(job, new Path(_stagingDir + "/output/"));

    job.getConfiguration().setInt(JobContext.NUM_MAPS, inputDataFiles.size());
    if (_dataSchema != null) {
      job.getConfiguration().set("data.schema", _dataSchema.toString());
    }
    setOutputPath(job.getConfiguration());

    job.setNumReduceTasks(0);
    for (Object key : _properties.keySet()) {
      job.getConfiguration().set(key.toString(), _properties.getProperty(key.toString()));
    }

    if (_depsJarPath != null && _depsJarPath.length() > 0) {
      addDepsJarToDistributedCache(new Path(_depsJarPath), job);
    }

    // Submit the job for execution.
    job.waitForCompletion(true);
    if (!job.isSuccessful()) {
      throw new RuntimeException("Job failed : " + job);
    }

    moveToOutputDirectory(fs);

    // Delete temporary directory.
    LOGGER.info("Cleanup the working directory.");
    LOGGER.info("Deleting the dir: {}", _stagingDir);
    fs.delete(new Path(_stagingDir), true);
  }

  protected void setAdditionalJobProperties(Job job) throws Exception {

  }

  protected void moveToOutputDirectory(FileSystem fs) throws Exception {
    LOGGER.info("Moving Segment Tar files from {} to: {}", _stagingDir + "/output/segmentTar", _outputDir);
    FileStatus[] segmentArr = fs.listStatus(new Path(_stagingDir + "/output/segmentTar"));
    for (FileStatus segment : segmentArr) {
      fs.rename(segment.getPath(), new Path(_outputDir, segment.getPath().getName()));
    }
  }

  protected Job setMapperClass(Job job) {
    job.setMapperClass(HadoopSegmentCreationMapReduceJob.HadoopSegmentCreationMapper.class);
    return job;

  }

  private void addDepsJarToDistributedCache(Path path, Job job) throws IOException {
    LOGGER.info("Trying to add all the deps jar files from directory: {}", path);
    FileSystem fs = FileSystem.get(getConf());
    FileStatus[] fileStatusArr = fs.listStatus(path);
    for (FileStatus fileStatus : fileStatusArr) {
      if (fileStatus.isDirectory()) {
        addDepsJarToDistributedCache(fileStatus.getPath(), job);
      } else {
        Path depJarPath = fileStatus.getPath();
        if (depJarPath.getName().endsWith(".jar")) {
          LOGGER.info("Adding deps jar files: {}", path);
          job.addCacheArchive(path.toUri());
        }
      }
    }
  }

  private ArrayList<FileStatus> getDataFilesFromPath(FileSystem fs, Path inBaseDir) throws IOException {
    ArrayList<FileStatus> dataFileStatusList = new ArrayList<FileStatus>();
    FileStatus[] fileStatusArr = fs.listStatus(inBaseDir);
    for (FileStatus fileStatus : fileStatusArr) {
      if (fileStatus.isDirectory()) {
        LOGGER.info("Trying to add all the data files from directory: {}", fileStatus.getPath());
        dataFileStatusList.addAll(getDataFilesFromPath(fs, fileStatus.getPath()));
      } else {
        String fileName = fileStatus.getPath().getName();
        if (fileName.endsWith(".avro")) {
          LOGGER.info("Adding avro files: {}", fileStatus.getPath());
          dataFileStatusList.add(fileStatus);
        }
        if (fileName.endsWith(".csv")) {
          LOGGER.info("Adding csv files: {}", fileStatus.getPath());
          dataFileStatusList.add(fileStatus);
        }
        if (fileName.endsWith(".json")) {
          LOGGER.info("Adding json files: {}", fileStatus.getPath());
          dataFileStatusList.add(fileStatus);
        }
      }
    }
    return dataFileStatusList;
  }

}
