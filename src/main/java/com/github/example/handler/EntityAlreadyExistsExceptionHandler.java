package com.github.example.handler;

import com.github.example.exception.EntityAlreadyExistsException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.hateos.JsonError;
import io.micronaut.http.hateos.Link;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {EntityAlreadyExistsException.class, ExceptionHandler.class})
public class EntityAlreadyExistsExceptionHandler implements ExceptionHandler<EntityAlreadyExistsException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, EntityAlreadyExistsException exception) {
        JsonError error = new JsonError(exception.getMessage())
                .link(Link.SELF, Link.of(request.getUri()));
        return HttpResponse.status(HttpStatus.CONFLICT).body(error);
    }
}
