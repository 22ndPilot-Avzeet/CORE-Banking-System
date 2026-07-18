package com.ebtps.controller;

import com.ebtps.authentication.SessionManager;
import com.ebtps.dao.UserDAO;
import com.ebtps.dao.UserDAOImpl;
import com.ebtps.exceptions.InvalidCredentialsException;
import com.ebtps.model.User;
import com.ebtps.view.MainFrame;

import javax.swing.JOptionPane;

/**
 * Controller responsible for handling login logic.
 */
public class LoginController {

    private final MainFrame mainFrame;
    private final UserDAO userDAO;

    public LoginController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.userDAO = new UserDAOImpl();
    }

    /**
     * Attempts to authenticate the user and navigate to the appropriate dashboard.
     * @param username The username input.
     * @param password The password input.
     */
    public void handleLogin(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, 
                    "Username and password cannot be empty.", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            User user = userDAO.authenticate(username, password);
            SessionManager.getInstance().login(user);
            
            // Navigate based on role
            if ("MANAGER".equals(user.getRole())) {
                mainFrame.showCard("MANAGER_DASHBOARD");
            } else {
                mainFrame.showCard("CUSTOMER_DASHBOARD");
            }
            
        } catch (InvalidCredentialsException e) {
            JOptionPane.showMessageDialog(mainFrame, 
                    e.getMessage(), 
                    "Login Failed", 
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, 
                    "An unexpected error occurred: " + e.getMessage(), 
                    "System Error", 
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
