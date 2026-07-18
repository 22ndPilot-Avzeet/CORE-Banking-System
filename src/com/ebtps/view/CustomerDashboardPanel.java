package com.ebtps.view;

import com.ebtps.controller.CustomerDashboardController;
import com.ebtps.utils.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Main panel for the Customer portal, containing sidebar navigation
 * and a content area powered by CardLayout.
 */
public class CustomerDashboardPanel extends JPanel {

    private final CustomerDashboardController controller;
    
    private final CardLayout contentCardLayout;
    private final JPanel contentPanel;
    
    private final AccountOverviewPanel accountOverviewPanel;
    private final CustomerProfilePanel customerProfilePanel;
    private final TransactionPanel transactionPanel;
    private final TransactionHistoryPanel transactionHistoryPanel;
    private final CustomerLoanPanel loanPanel;
    private final CustomerAnalyticsPanel analyticsPanel;

    public CustomerDashboardPanel(CustomerDashboardController controller) {
        this.controller = controller;
        this.contentCardLayout = new CardLayout();
        this.contentPanel = new JPanel(contentCardLayout);
        
        this.accountOverviewPanel = new AccountOverviewPanel();
        this.customerProfilePanel = new CustomerProfilePanel();
        this.transactionPanel = new TransactionPanel(controller);
        this.transactionHistoryPanel = new TransactionHistoryPanel(controller);
        this.loanPanel = new CustomerLoanPanel();
        this.analyticsPanel = new CustomerAnalyticsPanel();
        
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Theme.PRIMARY_BACKGROUND);

        // -- Sidebar (West) --
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(Theme.SECONDARY_BACKGROUND);
        sidebarPanel.setPreferredSize(new Dimension(200, 0));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER_COLOR));

        JLabel brandLabel = new JLabel("EBTPS Customer");
        brandLabel.setFont(Theme.BOLD_FONT);
        brandLabel.setForeground(Theme.PRIMARY_TEXT);
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        brandLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebarPanel.add(brandLabel);

        sidebarPanel.add(createNavButton("Dashboard", e -> showDashboard()));
        sidebarPanel.add(createNavButton("Transfer Funds", e -> showTransfer()));
        sidebarPanel.add(createNavButton("History", e -> showHistory()));
        sidebarPanel.add(createNavButton("Loans", e -> showLoans()));
        sidebarPanel.add(createNavButton("Analytics", e -> showAnalytics()));
        sidebarPanel.add(createNavButton("Profile", e -> showProfile()));
        
        // Add vertical glue to push logout to bottom
        sidebarPanel.add(Box.createVerticalGlue());
        
        sidebarPanel.add(createNavButton("Notifications", e -> showNotifications()));
        sidebarPanel.add(createNavButton("Logout", e -> controller.logout()));

        add(sidebarPanel, BorderLayout.WEST);

        // -- Content Area (Center) --
        contentPanel.setBackground(Theme.PRIMARY_BACKGROUND);
        
        contentPanel.add(accountOverviewPanel, "DASHBOARD");
        contentPanel.add(transactionPanel, "TRANSFER");
        contentPanel.add(transactionHistoryPanel, "HISTORY");
        contentPanel.add(loanPanel, "LOANS");
        contentPanel.add(analyticsPanel, "ANALYTICS");
        contentPanel.add(customerProfilePanel, "PROFILE");
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Auto-refresh when this panel is shown (e.g., after login)
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                showDashboard();
            }
        });
    }

    private JButton createNavButton(String text, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.NORMAL_FONT);
        btn.setForeground(Theme.PRIMARY_TEXT);
        btn.setBackground(Theme.SECONDARY_BACKGROUND);
        btn.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(200, 50));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addActionListener(action);
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(Theme.BORDER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Theme.SECONDARY_BACKGROUND);
            }
        });
        
        return btn;
    }

    public void showDashboard() {
        accountOverviewPanel.refreshData(controller.getAccountsForCurrentUser());
        contentCardLayout.show(contentPanel, "DASHBOARD");
    }
    
    public void showTransfer() {
        transactionPanel.refreshData();
        contentCardLayout.show(contentPanel, "TRANSFER");
    }
    
    public void showHistory() {
        transactionHistoryPanel.refreshData();
        contentCardLayout.show(contentPanel, "HISTORY");
    }

    public void showLoans() {
        loanPanel.refreshData();
        contentCardLayout.show(contentPanel, "LOANS");
    }
    
    public void showAnalytics() {
        analyticsPanel.refreshData();
        contentCardLayout.show(contentPanel, "ANALYTICS");
    }

    public void showProfile() {
        customerProfilePanel.refreshData();
        contentCardLayout.show(contentPanel, "PROFILE");
    }
    
    private void showNotifications() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        NotificationDialog dialog = new NotificationDialog(parentFrame);
        dialog.setVisible(true);
    }
}
