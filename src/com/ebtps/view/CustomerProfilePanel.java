package com.ebtps.view;

import com.ebtps.authentication.SessionManager;
import com.ebtps.model.User;
import com.ebtps.utils.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * Panel to display the profile details of the customer.
 */
public class CustomerProfilePanel extends JPanel {

    public CustomerProfilePanel() {
        initUI();
    }

    private void initUI() {
        setBackground(Theme.PRIMARY_BACKGROUND);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(Theme.HEADER_FONT);
        titleLabel.setForeground(Theme.PRIMARY_TEXT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(Theme.PRIMARY_BACKGROUND);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        addDetailRow(detailsPanel, "Name:", gbc);
        addDetailRow(detailsPanel, "Username:", gbc);
        addDetailRow(detailsPanel, "Email:", gbc);
        addDetailRow(detailsPanel, "Phone:", gbc);
        addDetailRow(detailsPanel, "Role:", gbc);

        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centerWrapper.setBackground(Theme.PRIMARY_BACKGROUND);
        centerWrapper.add(detailsPanel);
        
        add(centerWrapper, BorderLayout.CENTER);
    }

    private void addDetailRow(JPanel panel, String label, GridBagConstraints gbc) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.BOLD_FONT);
        lbl.setForeground(Theme.SECONDARY_TEXT);
        
        gbc.gridx = 0;
        panel.add(lbl, gbc);

        JLabel valLbl = new JLabel("");
        valLbl.setFont(Theme.NORMAL_FONT);
        valLbl.setForeground(Theme.PRIMARY_TEXT);
        
        // This is a temporary placeholder setup. 
        // In a real scenario, this gets populated when the panel is shown.
        // We will expose a method to update it.
        panel.add(valLbl, updateGbcValue(gbc));
        
        // Store reference to valLbl via client property to easily find it later
        panel.putClientProperty(label, valLbl);
    }
    
    private GridBagConstraints updateGbcValue(GridBagConstraints original) {
        GridBagConstraints copy = (GridBagConstraints) original.clone();
        copy.gridx = 1;
        original.gridy++;
        return copy;
    }

    /**
     * Refreshes the profile data.
     */
    public void refreshData() {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user != null) {
            JPanel detailsPanel = (JPanel) ((JPanel) ((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.CENTER)).getComponent(0);
            
            ((JLabel) detailsPanel.getClientProperty("Name:")).setText(user.getFullName());
            ((JLabel) detailsPanel.getClientProperty("Username:")).setText(user.getUsername());
            ((JLabel) detailsPanel.getClientProperty("Email:")).setText(user.getEmail());
            ((JLabel) detailsPanel.getClientProperty("Phone:")).setText(user.getPhone() != null ? user.getPhone() : "N/A");
            ((JLabel) detailsPanel.getClientProperty("Role:")).setText(user.getRole().name());
        }
    }
}
