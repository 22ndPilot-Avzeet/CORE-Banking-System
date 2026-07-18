package com.ebtps.view;

import com.ebtps.controller.CustomerAnalyticsController;
import com.ebtps.utils.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class CustomerAnalyticsPanel extends JPanel {

    private final CustomerAnalyticsController controller;
    private JTable analyticsTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;

    public CustomerAnalyticsPanel() {
        this.controller = new CustomerAnalyticsController();
        initUI();
    }

    private void initUI() {
        setBackground(Theme.PRIMARY_BACKGROUND);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Expense Analytics");
        titleLabel.setFont(Theme.HEADER_FONT);
        titleLabel.setForeground(Theme.PRIMARY_TEXT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        String[] cols = {"Category", "Total Spent (₹)"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        analyticsTable = new JTable(tableModel);
        analyticsTable.setFont(Theme.NORMAL_FONT);
        analyticsTable.setRowHeight(35);
        analyticsTable.getTableHeader().setFont(Theme.BOLD_FONT);

        JScrollPane scrollPane = new JScrollPane(analyticsTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(Theme.PRIMARY_BACKGROUND);
        totalLabel = new JLabel("Total Expenses: ₹0.00");
        totalLabel.setFont(Theme.SUBHEADER_FONT);
        totalLabel.setForeground(Theme.ERROR_COLOR);
        footerPanel.add(totalLabel);
        
        add(footerPanel, BorderLayout.SOUTH);
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        Map<String, Double> expenses = controller.getExpenseAnalytics();
        
        double grandTotal = 0;
        for (Map.Entry<String, Double> entry : expenses.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), String.format("%.2f", entry.getValue())});
            grandTotal += entry.getValue();
        }
        
        totalLabel.setText("Total Expenses: ₹" + String.format("%.2f", grandTotal));
    }
}
