package com.github.example.service;

import com.github.example.dto.request.CommandCreateTransaction;
import com.github.example.exception.EntityNotFoundException;
import com.github.example.model.Transaction;

import java.util.Collection;
import java.util.UUID;


public interface TransactionService {

    /**
     * Returns all transactions.
     *
     * @return the collection of all transactions
     */
    Collection<Transaction> getAll();

    /**
     * Returns transaction by the unique identifier of transaction.
     *
     * @param transactionId the unique identifier of transaction
     * @return the transaction found by unique identifier
     * @throws EntityNotFoundException if transaction is not found by unique identifier
     */
    Transaction getById(UUID transactionId);

    /**
     * Creates transaction with attributes specified by command.
     *
     * @param command dto with attributes for transaction creation
     * @return the successfully created transaction
     * @throws IllegalArgumentException if one of transaction participants
     *                                  is not found by the unique identifier,
     *                                  transaction between accounts with same identifier,
     *                                  amount for transaction is {@code null} or less than zero
     */
    Transaction createBy(CommandCreateTransaction command);
}
