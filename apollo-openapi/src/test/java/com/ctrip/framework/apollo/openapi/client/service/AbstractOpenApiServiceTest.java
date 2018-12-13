package com.ctrip.framework.apollo.openapi.client.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.ctrip.framework.apollo.openapi.client.constant.ApolloOpenApiConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
abstract class AbstractOpenApiServiceTest {
  @Mock
  protected CloseableHttpClient httpClient;
  @Mock
  protected CloseableHttpResponse someHttpResponse;
  @Mock
  protected StatusLine statusLine;

  protected Gson gson;

  protected String someBaseUrl;

  @Before
  public void setUp() throws Exception {
    gson = new GsonBuilder().setDateFormat(ApolloOpenApiConstants.JSON_DATE_FORMAT).create();
    someBaseUrl = "http://someBaseUrl";

    when(someHttpResponse.getStatusLine()).thenReturn(statusLine);
    when(statusLine.getStatusCode()).thenReturn(200);

    when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(someHttpResponse);
  }

}
