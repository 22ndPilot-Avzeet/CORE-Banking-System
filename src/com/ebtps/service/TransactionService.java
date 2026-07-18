package com.ebtps.service;

import com.ebtps.database.DatabaseConnection;
import com.ebtps.exceptions.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Handles the business logic for banking transactions.
 * Demonstrates JDBC Transaction Management (commit/rollback).
 */
public class TransactionService {

    /**
     * Executes a money transfer between two accounts using an atomic database transaction.
     */
    public void transferFunds(String sourceAccount, String targetAccount, BigDecimal amount) 
            throws InsufficientBalanceException, AccountFrozenException, 
                   AccountClosedException, DailyLimitExceededException, TransactionFailedException {
                   
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransactionFailedException("Transfer amount must be greater than zero.");
        }
        if (sourceAccount.equals(targetAccount)) {
            throw new TransactionFailedException("Cannot transfer to the same account.");
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            // Start Atomic Transaction
            conn.setAutoCommit(false);

            // 1. Lock and Verify Source Account (Pessimistic Locking using FOR UPDATE)
            BigDecimal sourceBalance = null;
            com.ebtps.model.AccountStatus sourceStatus = null;
            BigDecimal dailyLimit = null;
            try (PreparedStatement psSource = conn.prepareStatement(
                    "SELECT balance, status, daily_transfer_limit FROM Accounts WHERE account_number = ? FOR UPDATE")) {
                psSource.setString(1, sourceAccount);
                try (ResultSet rs = psSource.executeQuery()) {
                    if (rs.next()) {
                        sourceBalance = rs.getBigDecimal("balance");
                        sourceStatus = com.ebtps.model.AccountStatus.valueOf(rs.getString("status").toUpperCase());
                        dailyLimit = rs.getBigDecimal("daily_transfer_limit");
                    } else {
                        throw new TransactionFailedException("Source account not found.");
                    }
                }
            }

            // Validations for Source
            if (sourceStatus == com.ebtps.model.AccountStatus.FROZEN) throw new AccountFrozenException("Source account is frozen.");
            if (sourceStatus == com.ebtps.model.AccountStatus.CLOSED) throw new AccountClosedException("Source account is closed.");
            if (sourceBalance.compareTo(amount) < 0) throw new InsufficientBalanceException("Insufficient funds.");
            if (amount.compareTo(dailyLimit) > 0) throw new DailyLimitExceededException("Amount exceeds daily transfer limit.");

            // 2. Lock and Verify Target Account
            com.ebtps.model.AccountStatus targetStatus = null;
            try (PreparedStatement psTarget = conn.prepareStatement(
                    "SELECT status FROM Accounts WHERE account_number = ? FOR UPDATE")) {
                psTarget.setString(1, targetAccount);
                try (ResultSet rs = psTarget.executeQuery()) {
                    if (rs.next()) {
                        targetStatus = com.ebtps.model.AccountStatus.valueOf(rs.getString("status").toUpperCase());
                    } else {
                        throw new TransactionFailedException("Target account not found.");
                    }
                }
            }

            // Validations for Target
            if (targetStatus == com.ebtps.model.AccountStatus.FROZEN) throw new AccountFrozenException("Target account is frozen.");
            if (targetStatus == com.ebtps.model.AccountStatus.CLOSED) throw new AccountClosedException("Target account is closed.");

            // 3. Update Balances
            try (PreparedStatement psDeduct = conn.prepareStatement(
                    "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?")) {
                psDeduct.setBigDecimal(1, amount);
                psDeduct.setString(2, sourceAccount);
                psDeduct.executeUpdate();
            }

            try (PreparedStatement psAdd = conn.prepareStatement(
                    "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?")) {
                psAdd.setBigDecimal(1, amount);
                psAdd.setString(2, targetAccount);
                psAdd.executeUpdate();
            }

            // 4. Log Transaction
            String refNum = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            try (PreparedStatement psTxn = conn.prepareStatement(
                    "INSERT INTO Transactions (reference_number, source_account, destination_account, amount, transaction_type, status, category, description) " +
                    "VALUES (?, ?, ?, ?, 'TRANSFER', 'SUCCESS', 'Transfer', 'Online Transfer')")) {
                psTxn.setString(1, refNum);
                psTxn.setString(2, sourceAccount);
                psTxn.setString(3, targetAccount);
                psTxn.setBigDecimal(4, amount);
                psTxn.executeUpdate();
            }
            
            // 5. Send Notification to Target User (Asynchronously)
            int targetUserId = -1;
            try (PreparedStatement psUser = conn.prepareStatement("SELECT user_id FROM Accounts WHERE account_number = ?")) {
                psUser.setString(1, targetAccount);
                try (ResultSet rs = psUser.executeQuery()) {
                    if (rs.next()) targetUserId = rs.getInt("user_id");
                }
            }
            if (targetUserId != -1) {
                String message = "You received a transfer of ₹" + amount + " from account " + sourceAccount;
                NotificationService.getInstance().sendNotificationAsync(targetUserId, message);
            }

            // Commit Atomic Transaction
            conn.commit();

        } catch (SQLException e) {
            try {
                // Rollback in case of database error
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Fatal Error during rollback: " + ex.getMessage());
            }
            throw new TransactionFailedException("Database error during transfer: " + e.getMessage());
        } finally {
            try {
                // Restore auto-commit
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Failed to restore auto-commit: " + e.getMessage());
            }
        }
    }
}
