package com.github.example.service.impl

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.github.example.UnitTest
import com.github.example.dao.AccountDao
import com.github.example.dao.TransactionDao
import com.github.example.dto.request.CommandCreateTransaction
import com.github.example.exception.EntityNotFoundException
import com.github.example.model.Account
import com.github.example.model.Transaction
import org.junit.Test
import org.junit.experimental.categories.Category
import spock.lang.Specification

import static java.math.BigDecimal.ONE

@Category(UnitTest)
class TransactionServiceSpec extends Specification {

    @Subject
    TransactionServiceImpl transactionService

    @Collaborator
    TransactionDao transactionDao = Mock()
    @Collaborator
    AccountDao accountDao = Mock()

    def firstAccount = new Account(ONE)
    def secondAccount = new Account(ONE)
    def sourceAccountId = firstAccount.id
    def targetAccountId = secondAccount.id
    def amount = ONE
    def transaction = new Transaction(sourceAccountId, targetAccountId, amount)
    def transactionId = transaction.id
    def someUUID = UUID.fromString "00000000-0000-0000-0000-000000000000"

    def setup() {
        accountDao.getBy(sourceAccountId) >> firstAccount
        accountDao.getBy(targetAccountId) >> secondAccount
    }

    @Test
    def "should return empty collection of transactions when transactions storage returns empty collection"() {
        given:
        transactionDao.findAll() >> []

        when:
        def result = transactionService.getAll()

        then:
        result.empty
    }

    @Test
    def "should return not empty collection of transactions when transactions storage returns not empty collection"() {
        given:
        transactionDao.findAll() >> [transaction]

        when:
        def result = transactionService.getAll()

        then:
        !result.empty
    }

    @Test
    def "should throw exception when transactions storage doesn't contains entity for given id and throws exception"() {
        given:
        transactionDao.getBy(someUUID) >> { throw new EntityNotFoundException("Not found") }

        when:
        transactionService.getById someUUID

        then:
        thrown EntityNotFoundException
    }

    @Test
    def "should return transaction by given id when transactions storage contains entity for given id"() {
        given:
        transactionDao.getBy(transactionId) >> transaction

        when:
        def result = transactionService.getById transactionId

        then:
        result == transaction
    }

    @Test
    def "should throw exception when command for transaction creation is null"() {
        when:
        transactionService.createBy null

        then:
        thrown IllegalArgumentException
    }

    @Test
    def "should throw exception when accounts storage doesn't contains entity for given source account id and throws exception"() {
        given:
        def command = new CommandCreateTransaction(sourceAccountId: someUUID, targetAccountId: targetAccountId)
        and:
        accountDao.getBy(sourceAccountId) >> { throw new EntityNotFoundException("Not found") }

        when:
        transactionService.createBy command

        then:
        thrown IllegalArgumentException
    }

    @Test
    def "should throw exception when accounts storage doesn't contains entity for given target account id and throws exception"() {
        given:
        def command = new CommandCreateTransaction(sourceAccountId: sourceAccountId, targetAccountId: someUUID)
        and:
        accountDao.getBy(someUUID) >> { throw new EntityNotFoundException("Not found") }

        when:
        transactionService.createBy command

        then:
        thrown IllegalArgumentException
    }

    @Test
    def "should create transaction by given command and insert it into transactions storage when command for transaction creation is valid"() {
        given:
        def command = new CommandCreateTransaction(sourceAccountId: sourceAccountId, targetAccountId: targetAccountId, amount: amount)

        when:
        transactionService.createBy command

        then:
        1 * transactionDao.insert({
            it.sourceAccountId == sourceAccountId
            it.targetAccountId == targetAccountId
            it.amount == amount
        } as Transaction)
    }

    @Test
    def "should return created transaction when transaction successfully inserted and returned by transactions storage"() {
        given:
        def command = new CommandCreateTransaction(sourceAccountId: sourceAccountId, targetAccountId: targetAccountId, amount: amount)
        and:
        transactionDao.insert(_ as Transaction) >> transaction

        when:
        def result = transactionService.createBy command

        then:
        result == transaction
    }
}
