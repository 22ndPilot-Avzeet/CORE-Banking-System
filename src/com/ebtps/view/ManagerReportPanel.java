package com.ebtps.view;

import com.ebtps.controller.ManagerReportController;
import com.ebtps.utils.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class ManagerReportPanel extends JPanel {

    private final ManagerReportController controller;
    private JTable reportTable;
    private DefaultTableModel tableModel;

    public ManagerReportPanel() {
        this.controller = new ManagerReportController();
        initUI();
    }

    private void initUI() {
        setBackground(Theme.PRIMARY_BACKGROUND);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("System Reports");
        titleLabel.setFont(Theme.HEADER_FONT);
        titleLabel.setForeground(Theme.PRIMARY_TEXT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        String[] cols = {"Metric", "Value"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        reportTable = new JTable(tableModel);
        reportTable.setFont(Theme.NORMAL_FONT);
        reportTable.setRowHeight(40);
        reportTable.getTableHeader().setFont(Theme.BOLD_FONT);

        JScrollPane scrollPane = new JScrollPane(reportTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        Map<String, String> report = controller.getSystemReport();
        
        for (Map.Entry<String, String> entry : report.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }
}
