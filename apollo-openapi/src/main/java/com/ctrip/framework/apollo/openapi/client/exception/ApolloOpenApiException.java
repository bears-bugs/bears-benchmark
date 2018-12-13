package com.ctrip.framework.apollo.openapi.client.exception;

public class ApolloOpenApiException extends RuntimeException {

  public ApolloOpenApiException(int status, String reason, String message) {
    super(String.format("Request to apollo open api failed, status code: %d, reason: %s, message: %s", status, reason,
        message));
  }
}
