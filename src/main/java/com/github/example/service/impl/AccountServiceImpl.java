package com.github.example.service.impl;

import com.github.example.dao.AccountDao;
import com.github.example.dto.request.CommandCreateAccount;
import com.github.example.model.Account;
import com.github.example.service.AccountService;
import org.modelmapper.internal.util.Assert;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.UUID;

@Singleton
public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;

    @Inject
    public AccountServiceImpl(final AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Collection<Account> getAll() {
        return accountDao.findAll();
    }

    @Override
    public Account getById(final UUID accountId) {
        return accountDao.getBy(accountId);
    }

    @Override
    public Account createBy(final CommandCreateAccount command) {
        Assert.notNull(command);

        final Account account = new Account(command.getInitialBalance());
        return accountDao.insert(account);
    }
}