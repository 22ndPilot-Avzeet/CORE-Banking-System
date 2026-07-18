package com.ebtps.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Represents a Financial Transaction.
 */
public class Transaction implements Comparable<Transaction> {
    private final int id;
    private final String referenceNumber;
    private final String sourceAccount;
    private final String destinationAccount;
    private final BigDecimal amount;
    private final String transactionType; // DEPOSIT, WITHDRAWAL, TRANSFER
    private final String status; // SUCCESS, FAILED, PENDING
    private final String category;
    private final String description;
    private final Timestamp createdAt;

    public Transaction(int id, String referenceNumber, String sourceAccount, String destinationAccount, 
                       BigDecimal amount, String transactionType, String status, String category, 
                       String description, Timestamp createdAt) {
        this.id = id;
        this.referenceNumber = referenceNumber;
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.transactionType = transactionType;
        this.status = status;
        this.category = category;
        this.description = description;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public String getReferenceNumber() { return referenceNumber; }
    public String getSourceAccount() { return sourceAccount; }
    public String getDestinationAccount() { return destinationAccount; }
    public BigDecimal getAmount() { return amount; }
    public String getTransactionType() { return transactionType; }
    public String getStatus() { return status; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public Timestamp getCreatedAt() { return createdAt; }

    /**
     * Implementing Comparable to sort transactions naturally by date (newest first).
     */
    @Override
    public int compareTo(Transaction other) {
        if (this.createdAt == null || other.createdAt == null) return 0;
        return other.getCreatedAt().compareTo(this.createdAt); // Descending order
    }
}
