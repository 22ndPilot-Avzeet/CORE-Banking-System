package com.ebtps.view;

import com.ebtps.controller.ManagerDashboardController;
import com.ebtps.model.Account;
import com.ebtps.utils.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AccountManagementPanel extends JPanel {

    private final ManagerDashboardController controller;
    private JTextField searchField;
    private JPanel resultPanel;
    private JLabel accNumLabel;
    private JLabel statusLabel;
    private JButton freezeButton;
    private JButton unfreezeButton;
    
    private String currentSearchedAccount;

    public AccountManagementPanel(ManagerDashboardController controller) {
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        setBackground(Theme.PRIMARY_BACKGROUND);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Account Management");
        titleLabel.setFont(Theme.HEADER_FONT);
        titleLabel.setForeground(Theme.PRIMARY_TEXT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Theme.PRIMARY_BACKGROUND);

        // Search Bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Theme.PRIMARY_BACKGROUND);
        
        searchField = new JTextField(20);
        searchField.setFont(Theme.NORMAL_FONT);
        JButton searchButton = new JButton("Search Account");
        searchButton.setFont(Theme.BOLD_FONT);
        searchButton.setBackground(Theme.SECONDARY_BACKGROUND);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        searchButton.addActionListener(e -> searchAccount());

        searchPanel.add(new JLabel("Account Number: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Results Area
        resultPanel = new JPanel(new GridBagLayout());
        resultPanel.setBackground(Theme.PRIMARY_BACKGROUND);
        resultPanel.setVisible(false);
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0;

        accNumLabel = new JLabel("Account: ");
        accNumLabel.setFont(Theme.SUBHEADER_FONT);
        resultPanel.add(accNumLabel, gbc);

        gbc.gridy++;
        statusLabel = new JLabel("Status: ");
        statusLabel.setFont(Theme.NORMAL_FONT);
        resultPanel.add(statusLabel, gbc);

        gbc.gridy++;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(Theme.PRIMARY_BACKGROUND);
        
        freezeButton = new JButton("Freeze Account");
        freezeButton.setBackground(Theme.ERROR_COLOR);
        freezeButton.setForeground(Color.WHITE);
        
        unfreezeButton = new JButton("Unfreeze Account");
        unfreezeButton.setBackground(Theme.SUCCESS_COLOR);
        unfreezeButton.setForeground(Color.WHITE);
        
        freezeButton.addActionListener(e -> updateStatus("FROZEN"));
        unfreezeButton.addActionListener(e -> updateStatus("ACTIVE"));

        btnPanel.add(freezeButton);
        btnPanel.add(unfreezeButton);
        resultPanel.add(btnPanel, gbc);

        JPanel resultWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        resultWrapper.setBackground(Theme.PRIMARY_BACKGROUND);
        resultWrapper.add(resultPanel);

        mainPanel.add(resultWrapper, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void searchAccount() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) return;

        Account acc = controller.searchAccount(query);
        if (acc != null) {
            currentSearchedAccount = acc.getAccountNumber();
            accNumLabel.setText("Account: " + acc.getAccountNumber() + " (Type: " + acc.getAccountType() + ")");
            statusLabel.setText("Status: " + acc.getStatus());
            
            freezeButton.setVisible("ACTIVE".equals(acc.getStatus()));
            unfreezeButton.setVisible("FROZEN".equals(acc.getStatus()));
            
            resultPanel.setVisible(true);
        } else {
            resultPanel.setVisible(false);
            JOptionPane.showMessageDialog(this, "Account not found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateStatus(String newStatus) {
        if (currentSearchedAccount != null) {
            boolean success = controller.updateAccountStatus(currentSearchedAccount, newStatus);
            if (success) {
                JOptionPane.showMessageDialog(this, "Account status updated to " + newStatus);
                searchAccount(); // Refresh
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update account.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
