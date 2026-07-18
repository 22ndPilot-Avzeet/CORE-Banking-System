package com.ebtps.controller;

import com.ebtps.authentication.SessionManager;
import com.ebtps.dao.LoanDAO;
import com.ebtps.dao.LoanDAOImpl;
import com.ebtps.dao.ManagerDAO;
import com.ebtps.dao.ManagerDAOImpl;
import com.ebtps.model.Account;
import com.ebtps.model.Loan;
import com.ebtps.view.MainFrame;

import java.util.List;

public class ManagerDashboardController {

    private final MainFrame mainFrame;
    private final ManagerDAO managerDAO;
    private final LoanDAO loanDAO;

    public ManagerDashboardController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.managerDAO = new ManagerDAOImpl();
        this.loanDAO = new LoanDAOImpl();
    }

    public Account searchAccount(String accountNumber) {
        return managerDAO.getAccountByNumber(accountNumber);
    }

    public boolean updateAccountStatus(String accountNumber, String status) {
        return managerDAO.updateAccountStatus(accountNumber, status);
    }

    public List<Loan> getPendingLoans() {
        return loanDAO.getPendingLoans();
    }

    public boolean processLoan(int loanId, String status) {
        int managerId = SessionManager.getInstance().getCurrentUser().getId();
        return loanDAO.updateLoanStatus(loanId, status, managerId);
    }

    public void logout() {
        SessionManager.getInstance().logout();
        mainFrame.showCard("LOGIN_PANEL");
    }
}
