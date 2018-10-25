package com.github.example.model

import com.github.example.UnitTest
import org.junit.Test
import org.junit.experimental.categories.Category
import spock.lang.Specification

@Category(UnitTest)
class AccountSpec extends Specification {

    @Test
    def "should throw exception when trying initialize account with null balance"() {
        when:
        new Account(null)

        then:
        def ex = thrown IllegalArgumentException
        ex.message == "Initial balance cannot be null"
    }

    @Test
    def "should throw exception when trying initialize account with negative balance"() {
        given:
        def negativeBalance = new BigDecimal(-10)

        when:
        new Account(negativeBalance)

        then:
        def ex = thrown IllegalArgumentException
        ex.message == "Account balance should be positive or zero"
    }

    @Test
    def "should throw exception when balance is negative after deposit to account"() {
        given:
        def account = new Account(BigDecimal.ZERO)
        def negativeAmount = new BigDecimal(-10)

        when:
        account.deposit negativeAmount

        then:
        def ex = thrown IllegalArgumentException
        ex.message == "Account balance should be positive or zero"
    }

    @Test
    def "should throw exception when balance is negative after withdrawal from account"() {
        given:
        def account = new Account(BigDecimal.ZERO)
        def amount = BigDecimal.TEN

        when:
        account.withdraw amount

        then:
        def ex = thrown IllegalArgumentException
        ex.message == "Account balance should be positive or zero"
    }

    @Test
    def "should initialize account properties when initial balance is positive"() {
        given:
        def initialBalance = BigDecimal.ZERO

        when:
        def result = new Account(initialBalance)

        then:
        result.id
        result.balance == initialBalance
        result.createdAt == result.updatedAt
    }

    @Test
    def "should keep account immutable by returning new instance with copied and updated properties when deposit to account"() {
        given:
        def initialAccount = new Account(BigDecimal.ZERO)
        def amount = BigDecimal.TEN

        when:
        def result = initialAccount.deposit amount

        then:
        result != initialAccount
        result.id == initialAccount.id
        result.createdAt == initialAccount.createdAt
        result.balance == 10
    }
}
