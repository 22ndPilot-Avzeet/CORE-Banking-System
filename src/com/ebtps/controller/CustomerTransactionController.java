package com.ebtps.controller;

import com.ebtps.dao.TransactionDAO;
import com.ebtps.dao.TransactionDAOImpl;
import com.ebtps.exceptions.*;
import com.ebtps.model.Transaction;
import com.ebtps.service.TransactionService;

import javax.swing.JOptionPane;
import java.awt.Component;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller to handle customer transaction interactions.
 */
public class CustomerTransactionController {

    private final TransactionService transactionService;
    private final TransactionDAO transactionDAO;

    public CustomerTransactionController() {
        this.transactionService = new TransactionService();
        this.transactionDAO = new TransactionDAOImpl();
    }

    /**
     * Executes a transfer and handles any exceptions by displaying appropriate dialogs.
     */
    public boolean performTransfer(Component parentView, String sourceAccount, String targetAccount, String amountStr) {
        if (sourceAccount == null || sourceAccount.isEmpty()) {
            showError(parentView, "Please select a source account.");
            return false;
        }
        if (targetAccount == null || targetAccount.trim().isEmpty()) {
            showError(parentView, "Please enter a target account number.");
            return false;
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountStr);
        } catch (NumberFormatException e) {
            showError(parentView, "Invalid amount entered. Please enter a valid number.");
            return false;
        }

        try {
            transactionService.transferFunds(sourceAccount, targetAccount, amount);
            JOptionPane.showMessageDialog(parentView, 
                "Transfer of ₹" + amount + " to " + targetAccount + " successful!", 
                "Transfer Successful", JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (InsufficientBalanceException | AccountFrozenException | 
                 AccountClosedException | DailyLimitExceededException | TransactionFailedException e) {
            showError(parentView, e.getMessage());
            return false;
        } catch (Exception e) {
            showError(parentView, "An unexpected system error occurred: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Transaction> getTransactionHistory(String accountNumber) {
        return transactionDAO.getTransactionsByAccount(accountNumber);
    }

    private void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Transaction Error", JOptionPane.ERROR_MESSAGE);
    }
}
