package com.github.example.dao.impl

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.github.example.UnitTest
import com.github.example.exception.EntityAlreadyExistsException
import com.github.example.exception.EntityNotFoundException
import com.github.example.holder.LockHolder
import com.github.example.model.Account
import org.junit.Test
import org.junit.experimental.categories.Category
import spock.lang.Shared
import spock.lang.Specification

import static java.math.BigDecimal.ONE

@Category(UnitTest)
class InMemoryAccountDaoSpec extends Specification {

    @Subject
    InMemoryAccountDaoImpl inMemoryAccountDao

    @Collaborator
    LockHolder lockHolder = Mock()

    @Shared
    def account = new Account(ONE)
    def someUUID = UUID.fromString "0-0-0-0-0"

    @Test
    def "should throw exception when account for insertion is null"() {
        when:
        inMemoryAccountDao.insert null

        then:
        def ex = thrown IllegalArgumentException
        ex.message == "Account cannot be null"
    }

    @Test
    def "should throw exception when storage already contains account for this id"() {
        given:
        inMemoryAccountDao.insert account

        when:
        inMemoryAccountDao.insert account

        then:
        def ex = thrown EntityAlreadyExistsException
        ex.message == "Account already exists for id:" + account.id
    }

    @Test
    def "should return same account when it successfully inserted in storage"() {
        when:
        def result = inMemoryAccountDao.insert account

        then:
        result == account
    }

    @Test
    def "should return empty collection when accounts storage is empty"() {
        when:
        def result = inMemoryAccountDao.findAll()

        then:
        result.empty
    }

    @Test
    def "should return collection of stored accounts when accounts storage is not empty"() {
        given:
        inMemoryAccountDao.insert account

        when:
        def result = inMemoryAccountDao.findAll()

        then:
        !result.empty
        result[0] == account
    }

    @Test
    def "should throw exception when try get account by null instead of id"() {
        when:
        inMemoryAccountDao.getBy null

        then:
        def ex = thrown IllegalArgumentException
        ex.message == "Account identifier cannot be null"
    }

    @Test
    def "should throw exception when storage doesn't contains account for specified id"() {
        when:
        inMemoryAccountDao.getBy someUUID

        then:
        def ex = thrown EntityNotFoundException
        ex.message == "Account not exists for id:" + someUUID
    }

    @Test
    def "should return stored account when storage contains account for specified id"() {
        given:
        inMemoryAccountDao.insert account

        when:
        def result = inMemoryAccountDao.getBy account.id

        then:
        result == account
    }

    @Test
    def "should throw exception when account for update is null"() {
        when:
        inMemoryAccountDao.update null

        then:
        def ex = thrown IllegalArgumentException
        ex.message == "Account cannot be null"
    }

    @Test
    def "should acquire lock for account id using holder before updating account"() {
        given:
        inMemoryAccountDao.insert account

        when:
        inMemoryAccountDao.update account

        then:
        1 * lockHolder.acquire({ it.contains(account.id as String) } as String)
    }

    @Test
    def "should throw exception when try to update account that doesn't exist in storage"() {
        when:
        inMemoryAccountDao.update account

        then:
        def ex = thrown EntityNotFoundException
        ex.message == "Account not exists for id:" + account.id
    }

    @Test
    def "should update account when account for update already exists in storage"() {
        given:
        def updatedAccount = account.withdraw ONE
        and:
        inMemoryAccountDao.insert account

        when:
        inMemoryAccountDao.update updatedAccount

        then:
        inMemoryAccountDao.getBy(account.id) == updatedAccount
    }

    @Test
    def "should release lock for account id using holder when finished update of account"() {
        given:
        inMemoryAccountDao.insert account

        when:
        inMemoryAccountDao.update account

        then:
        1 * lockHolder.release({ it.contains(account.id as String) } as String)
    }

    @Test
    def "should throw exception when try lock account by null instead of id"() {
        when:
        inMemoryAccountDao.lockBy null

        then:
        def ex = thrown IllegalArgumentException
        ex.message == "Account identifier cannot be null"
    }

    @Test
    def "should acquire lock using holder when try to lock account for specified id"() {
        when:
        inMemoryAccountDao.lockBy someUUID

        then:
        1 * lockHolder.acquire({ it.contains(someUUID as String) } as String)
    }

    @Test
    def "should throw exception when try unlock account by null instead of id"() {
        when:
        inMemoryAccountDao.unlockBy null

        then:
        def ex = thrown IllegalArgumentException
        ex.message == "Account identifier cannot be null"
    }

    @Test
    def "should release lock using holder when try to unlock account for specified id"() {
        when:
        inMemoryAccountDao.unlockBy someUUID

        then:
        1 * lockHolder.release({ it.contains(someUUID as String) } as String)
    }
}
