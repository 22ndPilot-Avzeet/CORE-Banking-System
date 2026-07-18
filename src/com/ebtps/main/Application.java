package com.ebtps.main;

import com.ebtps.database.DatabaseConnection;
import com.ebtps.view.MainFrame;

import javax.swing.SwingUtilities;

/**
 * The main entry point for the Enterprise Banking Transaction Processing System (EBTPS).
 * Responsible for initializing the application on the Event Dispatch Thread (EDT).
 */
public class Application {
    
    public static void main(String[] args) {
        // Run the GUI creation on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                // Initialize Database Connection to ensure it's available early
                DatabaseConnection.getInstance();
                
                // Initialize and display the main frame
                MainFrame mainFrame = new MainFrame();
                
                // Initialize Controllers and Views for Milestone 2
                com.ebtps.controller.LoginController loginController = new com.ebtps.controller.LoginController(mainFrame);
                com.ebtps.view.LoginPanel loginPanel = new com.ebtps.view.LoginPanel(loginController);
                
                // Add the Login Panel to the MainFrame
                mainFrame.addCard(loginPanel, "LOGIN_PANEL");
                
                // Initialize Controllers and Views for Milestone 3
                com.ebtps.controller.CustomerDashboardController customerController = new com.ebtps.controller.CustomerDashboardController(mainFrame);
                com.ebtps.view.CustomerDashboardPanel customerDashboard = new com.ebtps.view.CustomerDashboardPanel(customerController);
                mainFrame.addCard(customerDashboard, "CUSTOMER_DASHBOARD");
                
                // Initialize Controllers and Views for Milestone 5
                com.ebtps.controller.ManagerDashboardController managerController = new com.ebtps.controller.ManagerDashboardController(mainFrame);
                com.ebtps.view.ManagerDashboardPanel managerDashboard = new com.ebtps.view.ManagerDashboardPanel(managerController);
                mainFrame.addCard(managerDashboard, "MANAGER_DASHBOARD");
                
                // Start AuditLogger Daemon Thread
                com.ebtps.service.AuditLogger auditLogger = new com.ebtps.service.AuditLogger();
                Thread auditThread = new Thread(auditLogger);
                auditThread.setDaemon(true); // Ensures thread stops when app closes
                auditThread.start();
                
                // Add Shutdown Hook for graceful termination
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    System.out.println("Application shutting down gracefully.");
                }));
                
                // Show the Login Panel by default
                mainFrame.showCard("LOGIN_PANEL");
                
                mainFrame.setVisible(true);
                
                System.out.println("EBTPS Application Started Successfully.");
            } catch (Exception e) {
                System.err.println("Critical failure during application startup: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
