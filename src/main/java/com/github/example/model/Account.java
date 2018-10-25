package com.github.example.model;


import org.modelmapper.internal.util.Assert;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * The {@code Account} class represents bank account in financial domain.
 * <p>
 * Accounts are immutable in particular implementation
 * <p>
 * The class {@code Account} includes methods for deposit and withdraw money from account balance.
 */
public final class Account {

    private final UUID id;
    private final BigDecimal balance;
    private final Instant createdAt;
    private final Instant updatedAt;

    public Account(final BigDecimal initialBalance) {
        Assert.notNull(initialBalance, "Initial balance");
        checkBalanceIsPositive(initialBalance);

        Instant currentInstant = Instant.now();
        this.id = UUID.randomUUID();
        this.createdAt = currentInstant;
        this.updatedAt = currentInstant;
        this.balance = initialBalance;
    }

    private Account(Account account, final BigDecimal changedBalance) {
        checkBalanceIsPositive(changedBalance);

        this.id = account.id;
        this.createdAt = account.createdAt;
        this.updatedAt = Instant.now();
        this.balance = changedBalance;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Account deposit(final BigDecimal amount) {
        return new Account(this, balance.add(amount));
    }

    public Account withdraw(final BigDecimal amount) {
        return new Account(this, balance.subtract(amount));
    }

    private void checkBalanceIsPositive(final BigDecimal balance) {
        Assert.isTrue(balance.compareTo(BigDecimal.ZERO) >= 0, "Account balance should be positive or zero");
    }
}
