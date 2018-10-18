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
package com.linkedin.pinot.common.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;




public class TarGzCompressionUtilsTest {
  private static final String SEGMENT_NAME = "mysegment";

  private File TEST_DIR;
  private File dataDir;
  private File tarDir;
  private File untarDir;
  private File segmentDir;

  @BeforeMethod
  public void setUpTest()
      throws IOException {
    TEST_DIR = Files.createTempDirectory(TarGzCompressionUtils.class.getName()).toFile();
    TEST_DIR.deleteOnExit();
    dataDir = new File(TEST_DIR, "dataDir");
    tarDir = new File(TEST_DIR, "tarDir");
    untarDir = new File(TEST_DIR, "untarDir");
    segmentDir = new File(dataDir, SEGMENT_NAME);
    FileUtils.forceMkdir(dataDir);
    FileUtils.forceMkdir(tarDir);
    FileUtils.forceMkdir(untarDir);
    FileUtils.forceMkdir(segmentDir);

  }

  @AfterMethod
  public void tearDownTest() {
    if (TEST_DIR != null) {
      FileUtils.deleteQuietly(TEST_DIR);
    }
  }


  @Test
  public void testBasic()
      throws IOException, InterruptedException, ArchiveException {
    File metaFile = new File(segmentDir, "metadata.properties");
    metaFile.createNewFile();
    File tarGzPath = new File(tarDir, SEGMENT_NAME + ".tar.gz");
    TarGzCompressionUtils.createTarGzOfDirectory(segmentDir.getPath(), tarGzPath.getPath(), "segmentId_");
    TarGzCompressionUtils.unTar(tarGzPath, untarDir);
    File[] files = untarDir.listFiles();
    Assert.assertNotNull(files);
    Assert.assertEquals(files.length, 1);
    File[] subFiles = files[0].listFiles();
    Assert.assertNotNull(subFiles);
    Assert.assertEquals(subFiles.length, 1);
    Assert.assertEquals(subFiles[0].getName(), "metadata.properties");
  }

  @Test
  public void testSubDirectories()
      throws IOException, ArchiveException, InterruptedException {
    new File(segmentDir, "metadata.properties").createNewFile();
    File v3Dir = new File(segmentDir, "v3");
    FileUtils.forceMkdir(v3Dir);
    new File(v3Dir, "creation.meta").createNewFile();

    File tarGzPath = new File(tarDir, SEGMENT_NAME + ".tar.gz");
    TarGzCompressionUtils.createTarGzOfDirectory(segmentDir.getPath(), tarGzPath.getPath());
    TarGzCompressionUtils.unTar(tarGzPath, untarDir);
    File[] segments = untarDir.listFiles();
    Assert.assertNotNull(segments);
    Assert.assertEquals(segments.length, 1);
    File[] segmentFiles = segments[0].listFiles();
    Assert.assertNotNull(segmentFiles);
    Assert.assertEquals(segmentFiles.length, 2);
    for (File segmentFile : segmentFiles) {
      String name = segmentFile.getName();
      Assert.assertTrue(name.equals("v3") || name.equals("metadata.properties"));
      if (name.equals("v3")) {
        Assert.assertTrue(segmentFile.isDirectory());
        File[] v3Files = v3Dir.listFiles();
        Assert.assertNotNull(v3Files);
        Assert.assertEquals(v3Files.length, 1);
        Assert.assertEquals(v3Files[0].getName(), "creation.meta");
      }
    }
  }

  @Test
  public void testEmptyDirectory()
      throws IOException, ArchiveException {
    File tarGzPath = new File(tarDir, SEGMENT_NAME + ".tar.gz");
    TarGzCompressionUtils.createTarGzOfDirectory(segmentDir.getPath(), tarGzPath.getPath());
    TarGzCompressionUtils.unTar(tarGzPath, untarDir);
    File[] segments = untarDir.listFiles();
    Assert.assertNotNull(segments);
    Assert.assertEquals(segments.length, 1);
    File[] segmentFiles = segments[0].listFiles();
    Assert.assertNotNull(segmentFiles);
    Assert.assertEquals(segmentFiles.length, 0);

  }
}
