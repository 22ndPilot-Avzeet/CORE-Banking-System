package com.ebtps.view;

import com.ebtps.controller.CustomerLoanController;
import com.ebtps.model.Loan;
import com.ebtps.utils.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerLoanPanel extends JPanel {

    private final CustomerLoanController controller;
    private JTextField amountField;
    private JTextField durationField;
    private JTable loanTable;
    private DefaultTableModel tableModel;

    public CustomerLoanPanel() {
        this.controller = new CustomerLoanController();
        initUI();
    }

    private void initUI() {
        setBackground(Theme.PRIMARY_BACKGROUND);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Top: Apply for loan
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Theme.PRIMARY_BACKGROUND);
        
        JLabel titleLabel = new JLabel("Loan Services");
        titleLabel.setFont(Theme.HEADER_FONT);
        titleLabel.setForeground(Theme.PRIMARY_TEXT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel applyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        applyPanel.setBackground(Theme.PRIMARY_BACKGROUND);
        applyPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR), "Apply for New Loan"));
        
        applyPanel.add(new JLabel("Amount (₹):"));
        amountField = new JTextField(10);
        applyPanel.add(amountField);
        
        applyPanel.add(new JLabel("Duration (Months):"));
        durationField = new JTextField(5);
        applyPanel.add(durationField);
        
        JButton applyBtn = new JButton("Submit Request");
        applyBtn.setBackground(Theme.SECONDARY_BACKGROUND);
        applyBtn.addActionListener(e -> applyLoan());
        applyPanel.add(applyBtn);
        
        topPanel.add(applyPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Center: Loan History
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(Theme.PRIMARY_BACKGROUND);
        historyPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JLabel historyLabel = new JLabel("My Loans");
        historyLabel.setFont(Theme.SUBHEADER_FONT);
        historyLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        historyPanel.add(historyLabel, BorderLayout.NORTH);

        String[] cols = {"Loan ID", "Amount (₹)", "Interest (%)", "Duration", "Status", "Date"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        loanTable = new JTable(tableModel);
        loanTable.setRowHeight(30);
        
        JScrollPane scrollPane = new JScrollPane(loanTable);
        historyPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(historyPanel, BorderLayout.CENTER);
    }

    private void applyLoan() {
        boolean success = controller.requestLoan(amountField.getText(), durationField.getText());
        if (success) {
            JOptionPane.showMessageDialog(this, "Loan request submitted successfully.");
            amountField.setText("");
            durationField.setText("");
            refreshData();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid input or error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<Loan> loans = controller.getMyLoans();
        for (Loan l : loans) {
            tableModel.addRow(new Object[]{
                l.getId(), String.format("%.2f", l.getAmount()), String.format("%.2f", l.getInterestRate()),
                l.getDurationMonths(), l.getStatus(), l.getRequestDate()
            });
        }
    }
}
