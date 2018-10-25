package com.github.example.model;

import org.modelmapper.internal.util.Assert;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * The {@code Transaction} class represents money transfer in financial domain.
 * <p>
 * Transaction are immutable in particular implementation.
 * <p>
 * The class {@code Transaction} includes methods for changing status of transaction according to {@link TransactionStatus} values.
 */
public final class Transaction {

    private final UUID id;
    private final UUID sourceAccountId;
    private final UUID targetAccountId;
    private final BigDecimal amount;
    private final TransactionStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final String reasonCode;

    public Transaction(final UUID sourceAccountId, final UUID targetAccountId, final BigDecimal amount) {
        Assert.notNull(amount, "Transaction amount");
        Assert.isTrue(amount.compareTo(BigDecimal.ZERO) > 0, "Transaction amount should be positive");
        Assert.isTrue(!sourceAccountId.equals(targetAccountId), "Transactions not allowed between same account id's");

        Instant currentInstant = Instant.now();
        this.id = UUID.randomUUID();
        this.createdAt = currentInstant;
        this.updatedAt = currentInstant;
        this.reasonCode = null;
        this.status = TransactionStatus.PENDING;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
    }

    private Transaction(final Transaction transaction, final TransactionStatus changedStatus, final String reasonCode) {
        this.id = transaction.id;
        this.createdAt = transaction.createdAt;
        this.updatedAt = Instant.now();
        this.status = changedStatus;
        this.reasonCode = reasonCode;
        this.sourceAccountId = transaction.sourceAccountId;
        this.targetAccountId = transaction.targetAccountId;
        this.amount = transaction.amount;
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UUID getSourceAccountId() {
        return sourceAccountId;
    }

    public UUID getTargetAccountId() {
        return targetAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public Transaction executed() {
        return new Transaction(this, TransactionStatus.SUCCESS, this.reasonCode);
    }

    public Transaction failed(final String reasonCode) {
        return new Transaction(this, TransactionStatus.FAILED, reasonCode);
    }

    public enum TransactionStatus {
        PENDING,
        SUCCESS,
        FAILED
    }
}
