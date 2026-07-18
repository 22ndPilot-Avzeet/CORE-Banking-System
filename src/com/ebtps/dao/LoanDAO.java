package com.ebtps.dao;

import com.ebtps.model.Loan;
import java.math.BigDecimal;
import java.util.List;

public interface LoanDAO {
    boolean requestLoan(int userId, BigDecimal amount, BigDecimal interestRate, int durationMonths);
    List<Loan> getLoansByUserId(int userId);
    List<Loan> getPendingLoans();
    boolean updateLoanStatus(int loanId, String status, int managerId);
}
