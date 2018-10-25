package com.github.example.service.impl;

import com.github.example.dao.AccountDao;
import com.github.example.dao.TransactionDao;
import com.github.example.dto.request.CommandCreateTransaction;
import com.github.example.exception.EntityNotFoundException;
import com.github.example.model.Transaction;
import com.github.example.service.TransactionService;
import org.modelmapper.internal.util.Assert;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.UUID;

@Singleton
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDao transactionDao;
    private final AccountDao accountDao;

    @Inject
    public TransactionServiceImpl(final TransactionDao transactionDao, final AccountDao accountDao) {
        this.transactionDao = transactionDao;
        this.accountDao = accountDao;
    }

    @Override
    public Collection<Transaction> getAll() {
        return transactionDao.findAll();
    }

    @Override
    public Transaction getById(final UUID transactionId) {
        return transactionDao.getBy(transactionId);
    }

    @Override
    public Transaction createBy(final CommandCreateTransaction command) {
        Assert.notNull(command);

        final UUID sourceAccountId = command.getSourceAccountId();
        final UUID targetAccountId = command.getTargetAccountId();

        checkAccountExistsById(sourceAccountId);
        checkAccountExistsById(targetAccountId);

        final Transaction transaction = new Transaction(sourceAccountId, targetAccountId, command.getAmount());
        return transactionDao.insert(transaction);
    }

    private void checkAccountExistsById(final UUID accountId) {
        try {
            accountDao.getBy(accountId);
        } catch (EntityNotFoundException ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }
}