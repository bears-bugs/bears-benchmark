package com.github.example.service;

import com.github.example.exception.CouldNotAcquireLockException;
import com.github.example.exception.EntityNotFoundException;
import com.github.example.model.Transaction.TransactionStatus;

import java.util.UUID;


public interface TransactionExecutionService {

    /**
     * Executes money transfer operation for transactions
     * in {@link TransactionStatus#PENDING} status with specified limit.
     *
     * @param limit the number of transaction to be fetched for execution
     */
    void executePending(int limit);

    /**
     * Executes operations in accordance with transaction
     * retrieved by the unique identifier of transaction.
     *
     * @param transactionId the unique identifier of transaction for execution
     * @throws CouldNotAcquireLockException if transaction is already locked by another thread
     * @throws EntityNotFoundException      if transaction is not found by unique identifier
     * @throws IllegalStateException        if transaction is already executed
     */
    void execute(UUID transactionId);
}
