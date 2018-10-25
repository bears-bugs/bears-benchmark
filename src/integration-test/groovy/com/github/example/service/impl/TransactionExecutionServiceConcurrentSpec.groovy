package com.github.example.service.impl

import com.github.example.IntegrationTest
import com.github.example.dto.request.CommandCreateAccount
import com.github.example.dto.request.CommandCreateTransaction
import com.github.example.service.AccountService
import com.github.example.service.TransactionExecutionService
import com.github.example.service.TransactionService
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.PropertySource
import org.junit.Test
import org.junit.experimental.categories.Category
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Timeout

import java.util.concurrent.CyclicBarrier
import java.util.concurrent.ExecutorCompletionService
import java.util.concurrent.Executors

@Category(IntegrationTest)
class TransactionExecutionServiceConcurrentSpec extends Specification {

    static CONFIGURATION = PropertySource.of(["processing.transactions.enabled": false, "processing.lockHolder.timeout": 5000])

    @Shared
    @AutoCleanup
    def applicationContext = ApplicationContext.run CONFIGURATION

    @Shared
    def executionService = applicationContext.getBean TransactionExecutionService
    @Shared
    def transactionService = applicationContext.getBean TransactionService
    @Shared
    def accountService = applicationContext.getBean AccountService

    @Test
    @Timeout(value = 10)
    def "should preserve consistency of summary balance when executes concurrent transactions between two accounts"() {
        given:
        def concurrentTransactionsCount = 1000
        def command = new CommandCreateAccount(initialBalance: 1000)
        def firstAccountId = accountService.createBy(command).id
        def secondAccountId = accountService.createBy(command).id
        def txFromFirstToSecond = { transaction firstAccountId, secondAccountId }
        def txFromSecondToFirst = { transaction secondAccountId, firstAccountId }

        when:
        executeConcurrentTransactions concurrentTransactionsCount, txFromFirstToSecond, txFromSecondToFirst

        then:
        accountService.getAll()*.balance.sum() == 2000
    }

    def transaction(def firstAccountId, def secondAccountId) {
        def command = new CommandCreateTransaction(sourceAccountId: firstAccountId, targetAccountId: secondAccountId, amount: 10)
        def transaction = transactionService.createBy command
        executionService.execute transaction.id
    }

    def executeConcurrentTransactions(int count, Closure txFromFirstToSecond, Closure txFromSecondToFirst) {
        def executor = Executors.newFixedThreadPool(count)
        def completionService = new ExecutorCompletionService<>(executor)
        def barrier = new CyclicBarrier(count)

        count.times {
            def closure = it % 2 == 0 ? txFromFirstToSecond : txFromSecondToFirst
            completionService.submit({
                barrier.await()
                closure.call()
            })
        }

        int executed = 0
        while (executed < count) {
            def future = completionService.take()
            if (future.done) executed++
        }
        executor.shutdown()
    }
}
