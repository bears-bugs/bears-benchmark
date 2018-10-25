package com.github.example.handler;

import com.github.example.exception.EntityNotFoundException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.hateos.JsonError;
import io.micronaut.http.hateos.Link;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {EntityNotFoundException.class, ExceptionHandler.class})
public class EntityNotFoundExceptionHandler implements ExceptionHandler<EntityNotFoundException, HttpResponse> {

    @Override
    public HttpResponse handle(HttpRequest request, EntityNotFoundException exception) {
        JsonError error = new JsonError(exception.getMessage())
                .link(Link.SELF, Link.of(request.getUri()));
        return HttpResponse.notFound(error);
    }
}
