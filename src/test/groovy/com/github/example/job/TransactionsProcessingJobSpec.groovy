package com.github.example.job

import com.blogspot.toomuchcoding.spock.subjcollabs.Collaborator
import com.blogspot.toomuchcoding.spock.subjcollabs.Subject
import com.github.example.UnitTest
import com.github.example.service.TransactionExecutionService
import org.junit.Test
import org.junit.experimental.categories.Category
import spock.lang.Specification

@Category(UnitTest)
class TransactionsProcessingJobSpec extends Specification {

    @Subject
    TransactionsProcessingJob transactionsProcessingJob

    @Collaborator
    TransactionExecutionService transactionExecutionService = Mock()
    @Collaborator
    boolean enabled = true
    @Collaborator
    int limit = 100

    @Test
    def "should start processing of pending transaction with specified limit when job is enabled and triggered by scheduler"() {
        when:
        transactionsProcessingJob.process()

        then:
        1 * transactionExecutionService.executePending(_)
    }
}
