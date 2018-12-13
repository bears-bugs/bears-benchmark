package com.ctrip.framework.apollo.core.utils;

import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by gl49 on 2018/6/8.
 */
public class NetUtil {

  private static final int DEFAULT_TIMEOUT_IN_SECONDS = 5000;

  /**
   * ping the url, return true if ping ok, false otherwise
   */
  public static boolean pingUrl(String address) {
    try {
      URL urlObj = new URL(address);
      HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
      connection.setRequestMethod("GET");
      connection.setUseCaches(false);
      connection.setConnectTimeout(DEFAULT_TIMEOUT_IN_SECONDS);
      connection.setReadTimeout(DEFAULT_TIMEOUT_IN_SECONDS);
      int statusCode = connection.getResponseCode();
      cleanUpConnection(connection);
      return (200 <= statusCode && statusCode <= 399);
    } catch (Throwable ignore) {
    }
    return false;
  }

  /**
   * according to https://docs.oracle.com/javase/7/docs/technotes/guides/net/http-keepalive.html, we should clean up the
   * connection by reading the response body so that the connection could be reused.
   */
  private static void cleanUpConnection(HttpURLConnection conn) {
    InputStreamReader isr = null;
    InputStreamReader esr = null;
    try {
      isr = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
      CharStreams.toString(isr);
    } catch (IOException e) {
      InputStream errorStream = conn.getErrorStream();

      if (errorStream != null) {
        esr = new InputStreamReader(errorStream, StandardCharsets.UTF_8);
        try {
          CharStreams.toString(esr);
        } catch (IOException ioe) {
          //ignore
        }
      }
    } finally {
      if (isr != null) {
        try {
          isr.close();
        } catch (IOException ex) {
          // ignore
        }
      }

      if (esr != null) {
        try {
          esr.close();
        } catch (IOException ex) {
          // ignore
        }
      }
    }
  }
}
