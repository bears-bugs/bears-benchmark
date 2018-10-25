package com.github.example.handler

import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.github.example.UnitTest
import com.github.example.exception.CouldNotAcquireLockException
import io.micronaut.http.HttpRequest
import io.micronaut.http.hateos.Link
import org.junit.Test
import org.junit.experimental.categories.Category
import spock.lang.Specification

import static io.micronaut.http.HttpResponse.uri
import static io.micronaut.http.HttpStatus.INTERNAL_SERVER_ERROR

@Category(UnitTest)
class CouldNotAcquireLockExceptionHandlerSpec extends Specification {

    @Subject
    CouldNotAcquireLockExceptionHandler exceptionHandler

    def errorMessage = "Some message"
    def request = Mock(HttpRequest)
    def exception = new CouldNotAcquireLockException(errorMessage)

    @Test
    def "should respond with internal server error code when handle exception"() {
        when:
        def result = exceptionHandler.handle request, exception

        then:
        result.status == INTERNAL_SERVER_ERROR
    }

    @Test
    def "should respond with error message in payload when handle exception"() {
        when:
        def result = exceptionHandler.handle request, exception

        then:
        result.body().message == errorMessage
    }

    @Test
    def "should respond with reference to requested uri in payload when handle exception"() {
        given:
        def expectedUri = uri("/some_uri")
        request.getUri() >> expectedUri

        when:
        def result = exceptionHandler.handle request, exception

        then:
        result.body().links.getFirst(Link.SELF).get().href == expectedUri
    }
}
