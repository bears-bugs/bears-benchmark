package com.github.example.dao;

import com.github.example.exception.CouldNotAcquireLockException;
import com.github.example.exception.EntityAlreadyExistsException;
import com.github.example.exception.EntityNotFoundException;
import com.github.example.model.Transaction;
import com.github.example.model.Transaction.TransactionStatus;

import java.util.Collection;
import java.util.UUID;


public interface TransactionDao {

    /**
     * Returns a collection of all transactions
     * sorted by creation date-time from the storage.
     *
     * @return all transactions in the storage
     */
    Collection<Transaction> findAll();

    /**
     * Returns a collection of transactions
     * sorted by creation date-time and limited by {@code limit}
     * with {@link TransactionStatus#PENDING} status from the storage.
     *
     * @return transactions int the storage filtered by predicate
     */
    Collection<Transaction> findPending(int limit);

    /**
     * Stores newly created transaction into the storage.
     *
     * @param transaction the transaction entity for storing into the storage
     * @return the transaction entity persisted into the storage
     * @throws EntityAlreadyExistsException if transaction with the unique identifier is already present
     */
    Transaction insert(Transaction transaction);

    /**
     * Returns transaction by the unique identifier of transaction
     * in case it's already present in the storage.
     *
     * @param transactionId the unique identifier of transaction
     * @return returns transaction found by unique identifier
     * @throws EntityNotFoundException if storage doesn't contain transaction with specified unique identifier
     */
    Transaction getBy(UUID transactionId);

    /**
     * Update transaction in the storage in case it already present
     * otherwise puts it into the storage.
     *
     * @param transaction the transaction entity for storing into the storage
     * @return the transaction entity updated in the storage
     */
    Transaction update(Transaction transaction);

    /**
     * Acquires the lock for transaction entity by the unique identifier.
     *
     * @param transactionId the unique identifier of transaction
     * @throws CouldNotAcquireLockException if lock is already acquired or thread is interrupted
     */
    void lockBy(UUID transactionId);

    /**
     * Releases lock for transaction entity by the unique identifier.
     *
     * @param transactionId the unique identifier of transaction
     */
    void unlockBy(UUID transactionId);
}
