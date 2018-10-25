package com.github.example.holder;

import com.github.example.exception.CouldNotAcquireLockException;


public interface LockHolder {

    /**
     * Acquires the lock for entity by the unique identificator.
     *
     * @param lockId the unique identificator of lock
     * @throws CouldNotAcquireLockException if lock is already acquired or thread is interrupted
     */
    void acquire(String lockId);

    /**
     * Releases lock for entity by the unique identificator.
     *
     * @param lockId the unique identificator of lock
     */
    void release(String lockId);
}
