package com.ebtps.view;

import com.ebtps.controller.CustomerDashboardController;
import com.ebtps.controller.CustomerTransactionController;
import com.ebtps.model.Account;
import com.ebtps.utils.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Panel to facilitate money transfers.
 */
public class TransactionPanel extends JPanel {

    private final CustomerTransactionController transactionController;
    private final CustomerDashboardController dashboardController;

    private JComboBox<String> sourceAccountComboBox;
    private JTextField targetAccountField;
    private JTextField amountField;
    private JButton transferButton;

    public TransactionPanel(CustomerDashboardController dashboardController) {
        this.dashboardController = dashboardController;
        this.transactionController = new CustomerTransactionController();
        initUI();
    }

    private void initUI() {
        setBackground(Theme.PRIMARY_BACKGROUND);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Transfer Funds");
        titleLabel.setFont(Theme.HEADER_FONT);
        titleLabel.setForeground(Theme.PRIMARY_TEXT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Theme.PRIMARY_BACKGROUND);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Source Account
        JLabel sourceLabel = new JLabel("From Account:");
        sourceLabel.setFont(Theme.NORMAL_FONT);
        formPanel.add(sourceLabel, gbc);

        gbc.gridx = 1;
        sourceAccountComboBox = new JComboBox<>();
        sourceAccountComboBox.setFont(Theme.NORMAL_FONT);
        formPanel.add(sourceAccountComboBox, gbc);

        // Target Account
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel targetLabel = new JLabel("To Account Number:");
        targetLabel.setFont(Theme.NORMAL_FONT);
        formPanel.add(targetLabel, gbc);

        gbc.gridx = 1;
        targetAccountField = new JTextField(20);
        targetAccountField.setFont(Theme.NORMAL_FONT);
        formPanel.add(targetAccountField, gbc);

        // Amount
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel amountLabel = new JLabel("Amount (₹):");
        amountLabel.setFont(Theme.NORMAL_FONT);
        formPanel.add(amountLabel, gbc);

        gbc.gridx = 1;
        amountField = new JTextField(20);
        amountField.setFont(Theme.NORMAL_FONT);
        formPanel.add(amountField, gbc);

        // Submit Button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        transferButton = new JButton("Execute Transfer");
        transferButton.setFont(Theme.BOLD_FONT);
        transferButton.setBackground(Theme.PRIMARY_BACKGROUND);
        transferButton.setForeground(Theme.PRIMARY_TEXT);
        transferButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        transferButton.addActionListener((ActionEvent e) -> {
            String selectedSource = (String) sourceAccountComboBox.getSelectedItem();
            if (selectedSource == null) return;
            String sourceAccNum = selectedSource.split(" - ")[0]; // Extract account number
            
            boolean success = transactionController.performTransfer(
                    this, sourceAccNum, targetAccountField.getText(), amountField.getText()
            );
            
            if (success) {
                targetAccountField.setText("");
                amountField.setText("");
                // Optionally auto-refresh source account balances in dropdown
                refreshData();
            }
        });

        formPanel.add(transferButton, gbc);

        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centerWrapper.setBackground(Theme.PRIMARY_BACKGROUND);
        centerWrapper.add(formPanel);
        
        add(centerWrapper, BorderLayout.CENTER);
    }

    /**
     * Called when the panel is shown to refresh available accounts.
     */
    public void refreshData() {
        sourceAccountComboBox.removeAllItems();
        List<Account> accounts = dashboardController.getAccountsForCurrentUser();
        for (Account acc : accounts) {
            if (acc.getStatus() != com.ebtps.model.AccountStatus.CLOSED) {
                sourceAccountComboBox.addItem(acc.getAccountNumber() + " - ₹" + String.format("%.2f", acc.getBalance()));
            }
        }
    }
}
