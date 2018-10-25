package com.github.example.holder.impl

import com.github.example.IntegrationTest
import com.github.example.exception.CouldNotAcquireLockException
import com.github.example.holder.LockHolder
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.PropertySource
import org.junit.Test
import org.junit.experimental.categories.Category
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.Executors

import static java.lang.Thread.currentThread

@Category(IntegrationTest)
class LockHolderConcurrentSpec extends Specification {

    static CONFIGURATION = PropertySource.of(["processing.transactions.enabled": false, "processing.lockHolder.timeout": 5000])

    @Shared
    @AutoCleanup
    def applicationContext = ApplicationContext.run CONFIGURATION

    @Shared
    def lockHolder = applicationContext.getBean LockHolder
    @Shared
    def executor = Executors.newSingleThreadExecutor()
    def lockId = "some_lock_id"

    def setup() {
        executor.submit({ lockHolder.acquire(lockId) }).get()
    }

    @Test
    def "should throw exception when lock is acquired by another thread and the waiting time elapsed before the lock could be acquired"() {
        when:
        lockHolder.acquire lockId

        then:
        def ex = thrown CouldNotAcquireLockException
        ex.message == "Couldn't acquire lock for key:" + lockId
    }

    @Test
    def "should throw exception when lock is acquired by another thread and the current thread is interrupted by another thread"() {
        given:
        def currentThread = currentThread()
        executor.execute({
            sleep 1000 // 5 times less than the waiting interval for timeout
            currentThread.interrupt()
        })

        when:
        lockHolder.acquire lockId

        then:
        def ex = thrown CouldNotAcquireLockException
        ex.message == "Lock not acquired due to interruption of thread, id:" + lockId
    }

    @Test
    def "should not throw exception when try release lock that is not acquired by current thread previously"() {
        when:
        lockHolder.release lockId

        then:
        notThrown Exception
    }
}
