package com.ebtps.view;

import com.ebtps.utils.Theme;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

/**
 * MainFrame serves as the single primary window for the application.
 * It uses a CardLayout to switch between different views (Login, Dashboard, etc.)
 * without opening multiple windows.
 */
public class MainFrame extends JFrame {
    
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public MainFrame() {
        setTitle("Enterprise Banking Transaction Processing System");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        getContentPane().setBackground(Theme.PRIMARY_BACKGROUND);

        // Initialize CardLayout and Main Panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Theme.PRIMARY_BACKGROUND);
        
        // Add a placeholder panel for Milestone 1
        JPanel placeholderPanel = new JPanel();
        placeholderPanel.setBackground(Theme.PRIMARY_BACKGROUND);
        mainPanel.add(placeholderPanel, "PLACEHOLDER");
        
        add(mainPanel);
    }

    /**
     * Switch the currently visible panel based on the card name.
     * @param cardName The name assigned to the panel when added to mainPanel.
     */
    public void showCard(String cardName) {
        cardLayout.show(mainPanel, cardName);
    }

    /**
     * Add a new panel to the CardLayout.
     */
    public void addCard(JPanel panel, String cardName) {
        mainPanel.add(panel, cardName);
    }
}
