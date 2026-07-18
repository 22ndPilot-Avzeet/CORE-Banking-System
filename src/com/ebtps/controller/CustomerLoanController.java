package com.ebtps.controller;

import com.ebtps.authentication.SessionManager;
import com.ebtps.dao.LoanDAO;
import com.ebtps.dao.LoanDAOImpl;
import com.ebtps.model.Loan;

import java.math.BigDecimal;
import java.util.List;

public class CustomerLoanController {

    private final LoanDAO loanDAO;

    public CustomerLoanController() {
        this.loanDAO = new LoanDAOImpl();
    }

    public boolean requestLoan(String amountStr, String durationStr) {
        try {
            BigDecimal amount = new BigDecimal(amountStr);
            int duration = Integer.parseInt(durationStr);
            
            if (amount.compareTo(BigDecimal.ZERO) <= 0 || duration <= 0) {
                return false;
            }
            
            // Hardcoded interest rate for simplicity
            BigDecimal interestRate = new BigDecimal("5.5"); 
            int userId = SessionManager.getInstance().getCurrentUser().getId();
            
            return loanDAO.requestLoan(userId, amount, interestRate, duration);
            
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public List<Loan> getMyLoans() {
        int userId = SessionManager.getInstance().getCurrentUser().getId();
        return loanDAO.getLoansByUserId(userId);
    }
}
