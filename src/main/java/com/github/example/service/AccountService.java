package com.github.example.service;


import com.github.example.dto.request.CommandCreateAccount;
import com.github.example.exception.EntityNotFoundException;
import com.github.example.model.Account;

import java.util.Collection;
import java.util.UUID;


public interface AccountService {

    /**
     * Returns all accounts.
     *
     * @return the collection of all accounts
     */
    Collection<Account> getAll();

    /**
     * Returns account by the unique identifier of account.
     *
     * @param accountId the unique identifier of account
     * @return the account found by unique identifier
     * @throws EntityNotFoundException if account is not found by unique identifier
     */
    Account getById(UUID accountId);

    /**
     * Creates account with attributes specified by command.
     *
     * @param command dto with attributes for account creation
     * @return the successfully created account
     * @throws IllegalArgumentException if initial balance for account creation
     *                                  is {@code null} or less than zero
     */
    Account createBy(CommandCreateAccount command);
}
