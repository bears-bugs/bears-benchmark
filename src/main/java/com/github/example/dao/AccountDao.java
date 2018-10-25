package com.github.example.dao;

import com.github.example.exception.CouldNotAcquireLockException;
import com.github.example.exception.EntityAlreadyExistsException;
import com.github.example.exception.EntityNotFoundException;
import com.github.example.model.Account;

import java.util.Collection;
import java.util.UUID;


public interface AccountDao {

    /**
     * Returns a collection of all accounts from the storage.
     *
     * @return all accounts in the storage
     */
    Collection<Account> findAll();

    /**
     * Stores newly created account into the storage.
     *
     * @param account the account entity for storing into the storage
     * @return the account entity persisted into the storage
     * @throws EntityAlreadyExistsException if account with the unique identifier is already present
     */
    Account insert(Account account);

    /**
     * Returns account by the unique identifier of account
     * in case it's already present in the storage.
     *
     * @param accountId the unique identifier of account
     * @return returns account found by unique identifier
     * @throws EntityNotFoundException if storage doesn't contain account with specified unique identifier
     */
    Account getBy(UUID accountId);

    /**
     * Update account in the storage in case it already present
     * otherwise puts it into the storage.
     *
     * @param account the account entity for storing into the storage
     * @return the account entity updated in the storage
     */
    Account update(Account account);

    /**
     * Acquires the lock for account entity by the unique identifier.
     *
     * @param accountId the unique identifier of account
     * @throws CouldNotAcquireLockException if lock is already acquired or thread is interrupted
     */
    void lockBy(UUID accountId);

    /**
     * Releases lock for account entity by the unique identifier.
     *
     * @param accountId the unique identifier of account
     */
    void unlockBy(UUID accountId);
}
