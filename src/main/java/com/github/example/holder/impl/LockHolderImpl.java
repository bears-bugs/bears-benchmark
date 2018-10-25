package com.github.example.holder.impl;

import com.github.example.exception.CouldNotAcquireLockException;
import com.github.example.holder.LockHolder;
import io.micronaut.context.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Singleton
public class LockHolderImpl implements LockHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(LockHolderImpl.class);

    private final ConcurrentMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    @Value("${processing.lockHolder.timeout:5000}")
    private long timeout;

    @Override
    public void acquire(final String lockId) {
        final ReentrantLock lock = locks.computeIfAbsent(lockId, key -> new ReentrantLock(true));
        acquireLock(lockId, lock);
    }

    @Override
    public void release(final String lockId) {
        Optional.ofNullable(locks.get(lockId))
                .ifPresent(lock -> releaseLock(lockId, lock));
    }

    private void acquireLock(final String lockId, final ReentrantLock lock) {
        try {
            if (!lock.tryLock(timeout, TimeUnit.MILLISECONDS)) {
                throw new CouldNotAcquireLockException("Couldn't acquire lock for key:" + lockId);
            }
            LOGGER.debug("Lock acquired for id:{}", lockId);
        } catch (InterruptedException ex) {
            final Thread currentThread = Thread.currentThread();
            LOGGER.error("Thread {} where interrupted when acquire lock for id:{}", currentThread.getName(), lockId);
            currentThread.interrupt();
            throw new CouldNotAcquireLockException("Lock not acquired due to interruption of thread, id:" + lockId, ex);
        }
    }

    private void releaseLock(final String lockId, final ReentrantLock lock) {
        if (lock.tryLock()) {
            locks.computeIfPresent(lockId, (key, value) -> {
                int holdCount = lock.getHoldCount();
                if (holdCount > 1) {
                    lock.unlock();
                    LOGGER.debug("Lock released for id:{}", lockId);
                }
                return holdCount == 1 ? null : lock;
            });
            lock.unlock();
        }
    }
}
