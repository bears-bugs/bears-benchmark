package com.github.example.service.impl

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.github.example.UnitTest
import com.github.example.dao.AccountDao
import com.github.example.dto.request.CommandCreateAccount
import com.github.example.exception.EntityNotFoundException
import com.github.example.model.Account
import org.junit.Test
import org.junit.experimental.categories.Category
import spock.lang.Specification

import static java.math.BigDecimal.ONE

@Category(UnitTest)
class AccountServiceSpec extends Specification {

    @Subject
    AccountServiceImpl accountService

    @Collaborator
    AccountDao accountDao = Mock()

    def account = new Account(ONE)
    def someUUID = UUID.fromString "00000000-0000-0000-0000-000000000000"

    @Test
    def "should return empty collection of accounts when accounts storage returns empty collection"() {
        given:
        accountDao.findAll() >> []

        when:
        def result = accountService.getAll()

        then:
        result.empty
    }

    @Test
    def "should return not empty collection of accounts when accounts storage returns not empty collection"() {
        given:
        def accounts = [account]
        accountDao.findAll() >> accounts

        when:
        def result = accountService.getAll()

        then:
        !result.empty
    }

    @Test
    def "should throw exception when accounts storage doesn't contains entity for given id and throws exception"() {
        given:
        accountDao.getBy(someUUID) >> { throw new EntityNotFoundException("Not found") }

        when:
        accountService.getById someUUID

        then:
        thrown EntityNotFoundException
    }

    @Test
    def "should return account by given id when accounts storage contains entity for given id"() {
        given:
        def accountId = account.id
        accountDao.getBy(accountId) >> account

        when:
        def result = accountService.getById accountId

        then:
        result == account
    }

    @Test
    def "should throw exception when command for account creation is null"() {
        when:
        accountService.createBy null

        then:
        thrown IllegalArgumentException
    }

    @Test
    def "should create account by given command and insert it into accounts storage when command for account creation is valid"() {
        given:
        def initialBalance = ONE
        def command = new CommandCreateAccount(initialBalance: initialBalance)

        when:
        accountService.createBy command

        then:
        1 * accountDao.insert({ it.balance == initialBalance } as Account)
    }

    @Test
    def "should return created account when account successfully inserted and returned by accounts storage"() {
        given:
        def command = new CommandCreateAccount(initialBalance: ONE)
        accountDao.insert(_ as Account) >> account

        when:
        def result = accountService.createBy command

        then:
        result == account
    }
}
