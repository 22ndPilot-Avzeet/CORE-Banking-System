package com.ebtps.controller;

import com.ebtps.authentication.SessionManager;
import com.ebtps.dao.AccountDAO;
import com.ebtps.dao.AccountDAOImpl;
import com.ebtps.model.Account;
import com.ebtps.model.User;
import com.ebtps.view.MainFrame;

import java.util.List;

/**
 * Controller for the Customer Dashboard.
 * Handles fetching data and view transitions within the customer portal.
 */
public class CustomerDashboardController {

    private final MainFrame mainFrame;
    private final AccountDAO accountDAO;

    public CustomerDashboardController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.accountDAO = new AccountDAOImpl();
    }

    /**
     * Retrieves the accounts for the currently logged-in user.
     */
    public List<Account> getAccountsForCurrentUser() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            return accountDAO.getAccountsByUserId(currentUser.getId());
        }
        return java.util.Collections.emptyList();
    }

    /**
     * Handles the logout action.
     */
    public void logout() {
        SessionManager.getInstance().logout();
        mainFrame.showCard("LOGIN_PANEL");
    }
}
