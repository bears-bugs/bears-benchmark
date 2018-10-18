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

import com.linkedin.pinot.common.utils.FileUploadDownloadClient.FileUploadType;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Random;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


@Test
public class FileUploadDownloadClientTest {
  private static final String TEST_HOST = "localhost";
  private static final int TEST_PORT = new Random().nextInt(10000) + 10000;
  private static final String TEST_URI = "http://testhost/segments/testSegment";
  private static HttpServer TEST_SERVER;

  @BeforeClass
  public void setup() throws Exception {
    TEST_SERVER = HttpServer.create(new InetSocketAddress(TEST_PORT), 0);
    TEST_SERVER.createContext("/segments", new testSegmentUploadHandler());
    TEST_SERVER.setExecutor(null); // creates a default executor
    TEST_SERVER.start();
  }

  static class testSegmentUploadHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
      Headers requestHeaders = httpExchange.getRequestHeaders();
      String uploadTypeStr = requestHeaders.getFirst(FileUploadDownloadClient.CustomHeaders.UPLOAD_TYPE);
      FileUploadType uploadType = FileUploadType.valueOf(uploadTypeStr);

      String downloadUri = null;
      if (uploadType == FileUploadType.JSON) {
        InputStream bodyStream = httpExchange.getRequestBody();
        try {
          JSONObject jsonObject = new JSONObject(IOUtils.toString(bodyStream, "UTF-8"));
          downloadUri = (String) jsonObject.get(CommonConstants.Segment.Offline.DOWNLOAD_URL);
        } catch (JSONException e) {
          throw new RuntimeException(e);
        }
        Assert.assertEquals(downloadUri, TEST_URI);
      } else if (uploadType == FileUploadType.URI) {
        downloadUri = requestHeaders.getFirst(FileUploadDownloadClient.CustomHeaders.DOWNLOAD_URI);
      } else {
        Assert.fail();
      }
      Assert.assertEquals(downloadUri, TEST_URI);
      sendResponse(httpExchange, HttpStatus.SC_OK, "OK");
    }

    public void sendResponse(HttpExchange httpExchange, int code, String response) throws IOException {
      httpExchange.sendResponseHeaders(code, response.length());
      OutputStream os = httpExchange.getResponseBody();
      os.write(response.getBytes());
      os.close();
    }
  }

  @AfterClass
  public void Shutdown() {
    TEST_SERVER.stop(0);
  }

  @Test
  public void testSendFileWithUri() throws Exception {
    try (FileUploadDownloadClient fileUploadDownloadClient = new FileUploadDownloadClient()) {
      Assert.assertEquals(fileUploadDownloadClient.sendSegmentUri(
          FileUploadDownloadClient.getUploadSegmentHttpURI(TEST_HOST, TEST_PORT), TEST_URI), HttpStatus.SC_OK);
    }
  }

  @Test
  public void testSendFileWithJson() throws Exception {
    JSONObject segmentJson = new JSONObject();
    segmentJson.put(CommonConstants.Segment.Offline.DOWNLOAD_URL, TEST_URI);
    String jsonString = segmentJson.toString();
    try (FileUploadDownloadClient fileUploadDownloadClient = new FileUploadDownloadClient()) {
      Assert.assertEquals(fileUploadDownloadClient.sendSegmentJson(
          FileUploadDownloadClient.getUploadSegmentHttpURI(TEST_HOST, TEST_PORT), jsonString), HttpStatus.SC_OK);
    }
  }
}
