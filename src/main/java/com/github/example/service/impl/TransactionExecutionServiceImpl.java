package com.github.example.service.impl;

import com.github.example.dao.AccountDao;
import com.github.example.dao.TransactionDao;
import com.github.example.exception.CouldNotAcquireLockException;
import com.github.example.exception.EntityNotFoundException;
import com.github.example.model.Account;
import com.github.example.model.Transaction;
import com.github.example.service.TransactionExecutionService;
import io.micronaut.core.util.CollectionUtils;
import org.modelmapper.internal.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import static com.github.example.model.Transaction.TransactionStatus.PENDING;
import static java.util.stream.Collectors.groupingBy;

@Singleton
public class TransactionExecutionServiceImpl implements TransactionExecutionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionExecutionServiceImpl.class);

    private final TransactionDao transactionDao;
    private final AccountDao accountDao;

    @Inject
    public TransactionExecutionServiceImpl(final TransactionDao transactionDao, final AccountDao accountDao) {
        this.transactionDao = transactionDao;
        this.accountDao = accountDao;
    }

    @Override
    public void executePending(final int limit) {
        final Collection<Transaction> pendingTransactions = transactionDao.findPending(limit);
        if (CollectionUtils.isNotEmpty(pendingTransactions)) {
            LOGGER.info("Found {} pending transactions for execution, limit for bulk operation is {}", pendingTransactions.size(), limit);
            getTxGroupedBySourceAccount(pendingTransactions).entrySet()
                    .parallelStream()
                    .forEach(entry -> executeWithOrdering(entry.getKey(), entry.getValue()));
        }
    }

    @Override
    public void execute(final UUID transactionId) {
        Assert.notNull(transactionId, "Transaction identifier");

        LOGGER.debug("Start execution of transaction with id:{} at {}", transactionId, Instant.now());
        transactionDao.lockBy(transactionId);

        try {
            Transaction transaction = transactionDao.getBy(transactionId);
            checkTransactionStatus(transaction);

            final UUID sourceAccountId = transaction.getSourceAccountId();
            final UUID targetAccountId = transaction.getTargetAccountId();
            final BigDecimal amount = transaction.getAmount();

            try {
                executeOperationsWithOrdering(sourceAccountId, targetAccountId, accountDao::lockBy);
                LOGGER.debug("Accounts with source id:{} and target id:{} locked during execution of transaction with id:{}", sourceAccountId, targetAccountId, transactionId);

                final Account sourceAccount = accountDao.getBy(sourceAccountId);
                final Account targetAccount = accountDao.getBy(targetAccountId);

                accountDao.update(sourceAccount.withdraw(amount));
                accountDao.update(targetAccount.deposit(amount));
                transactionDao.update(transaction.executed());
            } catch (CouldNotAcquireLockException ex) {
                LOGGER.debug("Execution of the transaction with id:{} will be delayed due to the lock on one of the accounts", transactionId, ex);
            } catch (Exception ex) {
                LOGGER.error("Failed to execute transaction with id:{} between accounts with source id:{} and target id:{}", transactionId, sourceAccountId, targetAccountId, ex);
                transactionDao.update(transaction.failed(ex.getMessage()));
            } finally {
                executeOperationsWithOrdering(sourceAccountId, targetAccountId, accountDao::unlockBy);
            }
        } finally {
            transactionDao.unlockBy(transactionId);
        }
        LOGGER.debug("Finished execution of transaction with id:{} at {}", transactionId, Instant.now());
    }

    private Map<UUID, List<Transaction>> getTxGroupedBySourceAccount(final Collection<Transaction> transactions) {
        return transactions.stream()
                .collect(groupingBy(Transaction::getSourceAccountId));
    }

    private void executeWithOrdering(final UUID sourceAccountId, final List<Transaction> transactions) {
        LOGGER.debug("Start execution of {} transactions with source account id:{}", transactions.size(), sourceAccountId);
        transactions.stream()
                .map(Transaction::getId)
                .forEach(this::executePendingTransaction);
    }

    private void executePendingTransaction(final UUID transactionId) {
        try {
            execute(transactionId);
        } catch (CouldNotAcquireLockException | EntityNotFoundException | IllegalStateException ex) {
            LOGGER.error("Execution of transaction with id:{} failed", transactionId, ex);
        }
    }

    private void checkTransactionStatus(final Transaction transaction) {
        if (!PENDING.equals(transaction.getStatus())) {
            throw new IllegalStateException("Transaction is already performed with id:" + transaction.getId());
        }
    }

    private void executeOperationsWithOrdering(final UUID sourceAccountId, final UUID targetAccountId, final Consumer<UUID> operation) {
        final UUID firstLock;
        final UUID secondLock;

        if (sourceAccountId.compareTo(targetAccountId) < 0) {
            firstLock = sourceAccountId;
            secondLock = targetAccountId;
        } else {
            firstLock = targetAccountId;
            secondLock = sourceAccountId;
        }

        operation.accept(firstLock);
        operation.accept(secondLock);
    }
}
