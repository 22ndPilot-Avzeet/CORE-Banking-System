package com.ebtps.view;

import com.ebtps.model.Account;
import com.ebtps.utils.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panel to display the overview of all customer accounts.
 */
public class AccountOverviewPanel extends JPanel {

    private final JPanel cardsContainer;

    public AccountOverviewPanel() {
        setBackground(Theme.PRIMARY_BACKGROUND);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Account Overview");
        titleLabel.setFont(Theme.HEADER_FONT);
        titleLabel.setForeground(Theme.PRIMARY_TEXT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        cardsContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        cardsContainer.setBackground(Theme.PRIMARY_BACKGROUND);
        
        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Theme.PRIMARY_BACKGROUND);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Refreshes the display with a list of accounts.
     */
    public void refreshData(List<Account> accounts) {
        cardsContainer.removeAll();

        if (accounts == null || accounts.isEmpty()) {
            JLabel emptyLabel = new JLabel("No accounts found.");
            emptyLabel.setFont(Theme.NORMAL_FONT);
            emptyLabel.setForeground(Theme.SECONDARY_TEXT);
            cardsContainer.add(emptyLabel);
        } else {
            // Demonstrating Collections Framework and Comparator
            java.util.Collections.sort(accounts, new java.util.Comparator<Account>() {
                @Override
                public int compare(Account a1, Account a2) {
                    // Sort descending by balance (highest first)
                    return a2.getBalance().compareTo(a1.getBalance());
                }
            });

            for (Account acc : accounts) {
                cardsContainer.add(createAccountCard(acc));
            }
        }

        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    private JPanel createAccountCard(Account account) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Theme.PRIMARY_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setPreferredSize(new Dimension(300, 150));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel typeLabel = new JLabel(account.getAccountType() + " ACCOUNT");
        typeLabel.setFont(Theme.SMALL_FONT);
        typeLabel.setForeground(Theme.SECONDARY_TEXT);
        card.add(typeLabel, gbc);

        gbc.gridy++;
        JLabel balanceLabel = new JLabel("₹" + String.format("%.2f", account.getBalance()));
        balanceLabel.setFont(new Font(Theme.FONT_FAMILY, Font.BOLD, 28));
        balanceLabel.setForeground(Theme.PRIMARY_TEXT);
        card.add(balanceLabel, gbc);

        gbc.gridy++;
        JLabel accNumLabel = new JLabel("Acc: " + account.getAccountNumber());
        accNumLabel.setFont(Theme.NORMAL_FONT);
        accNumLabel.setForeground(Theme.SECONDARY_TEXT);
        card.add(accNumLabel, gbc);
        
        gbc.gridy++;
        JLabel statusLabel = new JLabel("Status: " + account.getStatus());
        statusLabel.setFont(Theme.SMALL_FONT);
        
        if (account.getStatus() == com.ebtps.model.AccountStatus.ACTIVE) {
            statusLabel.setForeground(Theme.SUCCESS_COLOR);
        } else if (account.getStatus() == com.ebtps.model.AccountStatus.FROZEN) {
            statusLabel.setForeground(Theme.WARNING_COLOR);
        } else {
            statusLabel.setForeground(Theme.ERROR_COLOR);
        }
        
        card.add(statusLabel, gbc);

        return card;
    }
}
