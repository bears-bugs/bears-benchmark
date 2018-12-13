package com.hubspot.baragon.service.exceptions;

import java.util.Collections;

import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import io.dropwizard.jersey.errors.LoggingExceptionMapper;

@javax.ws.rs.ext.Provider
public class NotifyingExceptionMapper extends LoggingExceptionMapper<Exception> {
  private final BaragonExceptionNotifier notifier;

  @Inject
  public NotifyingExceptionMapper(BaragonExceptionNotifier notifier) {
    this.notifier = notifier;
  }

  @Override
  public Response toResponse(final Exception e) {
    final Response response = super.toResponse(e);

    if (response.getStatus() >= 500) {
      notifier.notify(e, Collections.<String, String>emptyMap());
    }

    return response;
  }
}

