package com.ebtps.view;

import com.ebtps.controller.ManagerDashboardController;
import com.ebtps.utils.Theme;

import javax.swing.*;
import java.awt.*;

public class ManagerDashboardPanel extends JPanel {

    private final ManagerDashboardController controller;
    private final CardLayout contentCardLayout;
    private final JPanel contentPanel;
    
    private final AccountManagementPanel accountPanel;
    private final ManagerLoanPanel loanPanel;
    private final ManagerReportPanel reportPanel;

    public ManagerDashboardPanel(ManagerDashboardController controller) {
        this.controller = controller;
        this.contentCardLayout = new CardLayout();
        this.contentPanel = new JPanel(contentCardLayout);
        
        this.accountPanel = new AccountManagementPanel(controller);
        this.loanPanel = new ManagerLoanPanel(controller);
        this.reportPanel = new ManagerReportPanel();
        
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Theme.PRIMARY_BACKGROUND);

        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(Theme.SECONDARY_BACKGROUND);
        sidebarPanel.setPreferredSize(new Dimension(200, 0));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER_COLOR));

        JLabel brandLabel = new JLabel("EBTPS Manager");
        brandLabel.setFont(Theme.BOLD_FONT);
        brandLabel.setForeground(Theme.PRIMARY_TEXT);
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        brandLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sidebarPanel.add(brandLabel);

        sidebarPanel.add(createNavButton("Accounts", e -> showAccounts()));
        sidebarPanel.add(createNavButton("Loans", e -> showLoans()));
        sidebarPanel.add(createNavButton("Reports", e -> showReports()));
        
        sidebarPanel.add(Box.createVerticalGlue());
        
        sidebarPanel.add(createNavButton("Notifications", e -> showNotifications()));
        sidebarPanel.add(createNavButton("Logout", e -> controller.logout()));

        add(sidebarPanel, BorderLayout.WEST);

        contentPanel.setBackground(Theme.PRIMARY_BACKGROUND);
        contentPanel.add(accountPanel, "ACCOUNTS");
        contentPanel.add(loanPanel, "LOANS");
        contentPanel.add(reportPanel, "REPORTS");
        
        add(contentPanel, BorderLayout.CENTER);
        
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                showAccounts();
            }
        });
    }

    private JButton createNavButton(String text, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.NORMAL_FONT);
        btn.setBackground(Theme.SECONDARY_BACKGROUND);
        btn.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(200, 50));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }

    public void showAccounts() {
        contentCardLayout.show(contentPanel, "ACCOUNTS");
    }

    public void showLoans() {
        loanPanel.refreshData();
        contentCardLayout.show(contentPanel, "LOANS");
    }
    
    public void showReports() {
        reportPanel.refreshData();
        contentCardLayout.show(contentPanel, "REPORTS");
    }
    
    private void showNotifications() {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        NotificationDialog dialog = new NotificationDialog(parentFrame);
        dialog.setVisible(true);
    }
}
