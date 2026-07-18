package com.ebtps.view;

import com.ebtps.controller.LoginController;
import com.ebtps.utils.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The login screen UI.
 * Follows a minimalist, monochrome design.
 */
public class LoginPanel extends JPanel {

    private final LoginController loginController;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginPanel(LoginController loginController) {
        this.loginController = loginController;
        initUI();
    }

    private void initUI() {
        setBackground(Theme.PRIMARY_BACKGROUND);
        setLayout(new GridBagLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Theme.PRIMARY_BACKGROUND);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel titleLabel = new JLabel("EBTPS Login", SwingConstants.CENTER);
        titleLabel.setFont(Theme.HEADER_FONT);
        titleLabel.setForeground(Theme.PRIMARY_TEXT);
        formPanel.add(titleLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(Theme.NORMAL_FONT);
        usernameLabel.setForeground(Theme.SECONDARY_TEXT);
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        usernameField.setFont(Theme.NORMAL_FONT);
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(Theme.NORMAL_FONT);
        passwordLabel.setForeground(Theme.SECONDARY_TEXT);
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(Theme.NORMAL_FONT);
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        loginButton = new JButton("Secure Login");
        loginButton.setFont(Theme.BOLD_FONT);
        loginButton.setBackground(Theme.PRIMARY_BACKGROUND);
        loginButton.setForeground(Theme.PRIMARY_TEXT);
        loginButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.PRIMARY_TEXT, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                loginController.handleLogin(username, password);
            }
        });

        // Add hover effect
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(Theme.SECONDARY_BACKGROUND);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(Theme.PRIMARY_BACKGROUND);
            }
        });

        formPanel.add(loginButton, gbc);

        add(formPanel);
    }
}
