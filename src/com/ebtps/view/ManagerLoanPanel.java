package com.ebtps.view;

import com.ebtps.controller.ManagerDashboardController;
import com.ebtps.model.Loan;
import com.ebtps.utils.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManagerLoanPanel extends JPanel {

    private final ManagerDashboardController controller;
    private JTable loanTable;
    private DefaultTableModel tableModel;

    public ManagerLoanPanel(ManagerDashboardController controller) {
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        setBackground(Theme.PRIMARY_BACKGROUND);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Pending Loan Approvals");
        titleLabel.setFont(Theme.HEADER_FONT);
        titleLabel.setForeground(Theme.PRIMARY_TEXT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        String[] cols = {"Loan ID", "User ID", "Amount (₹)", "Interest (%)", "Duration (M)", "Request Date"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        loanTable = new JTable(tableModel);
        loanTable.setFont(Theme.NORMAL_FONT);
        loanTable.setRowHeight(30);
        loanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(loanTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(Theme.PRIMARY_BACKGROUND);
        
        JButton approveBtn = new JButton("Approve Selected");
        approveBtn.setBackground(Theme.SUCCESS_COLOR);
        approveBtn.setForeground(Color.WHITE);
        
        JButton rejectBtn = new JButton("Reject Selected");
        rejectBtn.setBackground(Theme.ERROR_COLOR);
        rejectBtn.setForeground(Color.WHITE);

        approveBtn.addActionListener(e -> processSelected("APPROVED"));
        rejectBtn.addActionListener(e -> processSelected("REJECTED"));

        actionPanel.add(approveBtn);
        actionPanel.add(rejectBtn);
        
        add(actionPanel, BorderLayout.SOUTH);
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<Loan> pending = controller.getPendingLoans();
        for (Loan l : pending) {
            tableModel.addRow(new Object[]{
                l.getId(), l.getUserId(), String.format("%.2f", l.getAmount()),
                String.format("%.2f", l.getInterestRate()), l.getDurationMonths(), l.getRequestDate()
            });
        }
    }

    private void processSelected(String status) {
        int selectedRow = loanTable.getSelectedRow();
        if (selectedRow >= 0) {
            int loanId = (int) tableModel.getValueAt(selectedRow, 0);
            boolean success = controller.processLoan(loanId, status);
            if (success) {
                JOptionPane.showMessageDialog(this, "Loan " + status);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Error processing loan.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a loan first.");
        }
    }
}
