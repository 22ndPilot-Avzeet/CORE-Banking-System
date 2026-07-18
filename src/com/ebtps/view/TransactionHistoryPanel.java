package com.ebtps.view;

import com.ebtps.controller.CustomerDashboardController;
import com.ebtps.controller.CustomerTransactionController;
import com.ebtps.model.Account;
import com.ebtps.model.Transaction;
import com.ebtps.utils.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Panel to display the transaction history for a selected account.
 */
public class TransactionHistoryPanel extends JPanel {

    private final CustomerDashboardController dashboardController;
    private final CustomerTransactionController transactionController;

    private JComboBox<String> accountComboBox;
    private JTable transactionTable;
    private DefaultTableModel tableModel;

    public TransactionHistoryPanel(CustomerDashboardController dashboardController) {
        this.dashboardController = dashboardController;
        this.transactionController = new CustomerTransactionController();
        initUI();
    }

    private void initUI() {
        setBackground(Theme.PRIMARY_BACKGROUND);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header Panel with Title and Account Selector
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.PRIMARY_BACKGROUND);
        
        JLabel titleLabel = new JLabel("Transaction History");
        titleLabel.setFont(Theme.HEADER_FONT);
        titleLabel.setForeground(Theme.PRIMARY_TEXT);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        selectorPanel.setBackground(Theme.PRIMARY_BACKGROUND);
        JLabel selectLabel = new JLabel("Select Account:");
        selectLabel.setFont(Theme.NORMAL_FONT);
        selectorPanel.add(selectLabel);

        accountComboBox = new JComboBox<>();
        accountComboBox.setFont(Theme.NORMAL_FONT);
        accountComboBox.addActionListener((ActionEvent e) -> loadTransactionsForSelectedAccount());
        selectorPanel.add(accountComboBox);

        headerPanel.add(selectorPanel, BorderLayout.EAST);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Date", "Ref Number", "Type", "Source", "Target", "Amount (₹)", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only table
            }
        };
        
        transactionTable = new JTable(tableModel);
        transactionTable.setFont(Theme.NORMAL_FONT);
        transactionTable.getTableHeader().setFont(Theme.BOLD_FONT);
        transactionTable.setRowHeight(30);
        transactionTable.setFillsViewportHeight(true);
        transactionTable.setShowGrid(false);
        transactionTable.setIntercellSpacing(new Dimension(0, 0));
        
        // Simple zebra striping alternative using selection color
        transactionTable.setSelectionBackground(Theme.SECONDARY_BACKGROUND);
        transactionTable.setSelectionForeground(Theme.PRIMARY_TEXT);

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR));
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Refreshes the dropdown with available accounts.
     */
    public void refreshData() {
        accountComboBox.removeAllItems();
        List<Account> accounts = dashboardController.getAccountsForCurrentUser();
        for (Account acc : accounts) {
            accountComboBox.addItem(acc.getAccountNumber());
        }
        if (accountComboBox.getItemCount() > 0) {
            accountComboBox.setSelectedIndex(0); // This triggers action listener to load transactions
        } else {
            tableModel.setRowCount(0); // Clear table
        }
    }

    private void loadTransactionsForSelectedAccount() {
        String selectedAccount = (String) accountComboBox.getSelectedItem();
        if (selectedAccount == null) return;

        List<Transaction> history = transactionController.getTransactionHistory(selectedAccount);
        
        // Demonstrate Comparable interface by sorting
        java.util.Collections.sort(history);
        
        tableModel.setRowCount(0); // Clear existing rows
        
        for (Transaction tx : history) {
            Object[] row = {
                tx.getCreatedAt().toString(),
                tx.getReferenceNumber(),
                tx.getTransactionType(),
                tx.getSourceAccount(),
                tx.getDestinationAccount() != null ? tx.getDestinationAccount() : "-",
                String.format("%.2f", tx.getAmount()),
                tx.getStatus()
            };
            tableModel.addRow(row);
        }
    }
}
