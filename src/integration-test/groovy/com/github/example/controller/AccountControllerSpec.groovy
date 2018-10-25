package com.github.example.controller

import com.github.example.IntegrationTest
import com.github.example.dto.request.CommandCreateAccount
import com.github.example.dto.response.AccountData
import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import org.junit.Test
import org.junit.experimental.categories.Category
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

@Category(IntegrationTest)
class AccountControllerSpec extends Specification {

    static final ACCOUNT_RESOURCE_URI = "/api/1.0/accounts"

    @Shared
    @AutoCleanup
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)
    @Shared
    @AutoCleanup
    HttpClient client = embeddedServer.applicationContext.createBean(HttpClient, embeddedServer.getURL())

    @Test
    def "should return empty collection with 200 status code when accounts not exists yet"() {
        given:
        def request = HttpRequest.GET ACCOUNT_RESOURCE_URI

        when:
        def response = client.toBlocking().exchange request, Collection.class

        then:
        response.status == HttpStatus.OK
        response.body().empty
    }

    @Test
    def "should return error response with 404 status code when account with specified id doesn't exist"() {
        given:
        def request = HttpRequest.GET ACCOUNT_RESOURCE_URI + "/a-b-c-d-e"

        when:
        client.toBlocking().exchange request

        then:
        def ex = thrown HttpClientResponseException
        ex.status == HttpStatus.NOT_FOUND
        ex.message == "Account not exists for id:0000000a-000b-000c-000d-00000000000e"
    }

    @Test
    def "should return error response with 400 status code when trying to get account with incorrect id format"() {
        given:
        def request = HttpRequest.GET ACCOUNT_RESOURCE_URI + "/1"

        when:
        client.toBlocking().exchange request

        then:
        def ex = thrown HttpClientResponseException
        ex.status == HttpStatus.BAD_REQUEST
        ex.message == "Failed to convert argument [accountId] for value [1] due to: Invalid UUID string: 1"
    }

    @Test
    def "should return error with 400 status code when trying to create account with null balance"() {
        when:
        createAccount null

        then:
        def ex = thrown HttpClientResponseException
        ex.status == HttpStatus.BAD_REQUEST
        ex.message == "Initial balance cannot be null"
    }

    @Test
    def "should return error with 400 status code when trying to create account with negative balance"() {
        when:
        createAccount(-100)

        then:
        def ex = thrown HttpClientResponseException
        ex.status == HttpStatus.BAD_REQUEST
        ex.message == "Account balance should be positive or zero"
    }

    @Test
    def "should return account data with 201 status code and location header when create account by valid command"() {
        given:
        def initialBalance = 500
        def command = new CommandCreateAccount(initialBalance: 500)
        def request = HttpRequest.POST ACCOUNT_RESOURCE_URI, command

        when:
        def response = client.toBlocking().exchange request, AccountData.class

        then:
        response.status == HttpStatus.CREATED
        response.body().balance == initialBalance
        response.header("Location") == ACCOUNT_RESOURCE_URI + "/" + response.body().id
    }

    @Test
    def "should return account data with 200 status code when account with specified id exist"() {
        given:
        def initialBalance = 100
        def accountId = createAccount initialBalance
        and:
        def request = HttpRequest.GET ACCOUNT_RESOURCE_URI + "/" + accountId

        when:
        def response = client.toBlocking().exchange request, AccountData.class

        then:
        response.status == HttpStatus.OK
        response.body().id == accountId
        response.body().balance == initialBalance
        response.body().createdAt == response.body().updatedAt
    }

    @Test
    def "should return collection of accounts data with 200 status code when accounts exists"() {
        given:
        def request = HttpRequest.GET ACCOUNT_RESOURCE_URI
        and:
        createAccount 100

        when:
        def response = client.toBlocking().exchange request, Collection.class

        then:
        response.status == HttpStatus.OK
        !response.body().empty
    }

    def createAccount(def initialBalance) {
        def command = new CommandCreateAccount(initialBalance: initialBalance)
        def request = HttpRequest.POST ACCOUNT_RESOURCE_URI, command

        def response = client.toBlocking().exchange request, AccountData.class

        response.body().id
    }
}
