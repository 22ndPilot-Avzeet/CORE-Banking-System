package com.ebtps.model;

import java.math.BigDecimal;

/**
 * Represents a Bank Account in the EBTPS application.
 * Demonstrates encapsulation.
 */
public class Account {
    private final String accountNumber;
    private final int userId;
    private final String accountType; // e.g., SAVINGS, CHECKING
    private final BigDecimal balance;
    private final AccountStatus status; // ACTIVE, FROZEN, CLOSED
    private final BigDecimal dailyTransferLimit;

    public Account(String accountNumber, int userId, String accountType, BigDecimal balance, AccountStatus status, BigDecimal dailyTransferLimit) {
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.accountType = accountType;
        this.balance = balance;
        this.status = status;
        this.dailyTransferLimit = dailyTransferLimit;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public int getUserId() {
        return userId;
    }

    public String getAccountType() {
        return accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public BigDecimal getDailyTransferLimit() {
        return dailyTransferLimit;
    }
}
