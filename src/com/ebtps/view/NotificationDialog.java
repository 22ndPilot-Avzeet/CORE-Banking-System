package com.ebtps.view;

import com.ebtps.authentication.SessionManager;
import com.ebtps.dao.NotificationDAO;
import com.ebtps.dao.NotificationDAOImpl;
import com.ebtps.model.Notification;
import com.ebtps.utils.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NotificationDialog extends JDialog {

    private final NotificationDAO notificationDAO;
    private final JPanel listPanel;

    public NotificationDialog(Frame parent) {
        super(parent, "Notifications", true);
        this.notificationDAO = new NotificationDAOImpl();
        
        setSize(400, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.PRIMARY_BACKGROUND);
        
        JLabel titleLabel = new JLabel("Unread Notifications", SwingConstants.CENTER);
        titleLabel.setFont(Theme.SUBHEADER_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(titleLabel, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Theme.PRIMARY_BACKGROUND);
        
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());
        JPanel footer = new JPanel();
        footer.setBackground(Theme.PRIMARY_BACKGROUND);
        footer.add(closeBtn);
        add(footer, BorderLayout.SOUTH);
        
        loadNotifications();
    }

    private void loadNotifications() {
        listPanel.removeAll();
        int userId = SessionManager.getInstance().getCurrentUser().getId();
        List<Notification> notifications = notificationDAO.getUnreadNotifications(userId);

        if (notifications.isEmpty()) {
            JLabel emptyLabel = new JLabel("No new notifications.");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyLabel.setFont(Theme.NORMAL_FONT);
            listPanel.add(Box.createVerticalStrut(20));
            listPanel.add(emptyLabel);
        } else {
            for (Notification n : notifications) {
                listPanel.add(createNotificationCard(n));
                listPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createNotificationCard(Notification n) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Theme.SECONDARY_BACKGROUND);
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setMaximumSize(new Dimension(380, 80));

        JTextArea msgArea = new JTextArea(n.getMessage());
        msgArea.setWrapStyleWord(true);
        msgArea.setLineWrap(true);
        msgArea.setEditable(false);
        msgArea.setBackground(Theme.SECONDARY_BACKGROUND);
        msgArea.setFont(Theme.SMALL_FONT);
        card.add(msgArea, BorderLayout.CENTER);

        JLabel dateLabel = new JLabel(n.getCreatedAt().toString());
        dateLabel.setFont(Theme.SMALL_FONT);
        dateLabel.setForeground(Theme.SECONDARY_TEXT);
        card.add(dateLabel, BorderLayout.NORTH);

        JButton markReadBtn = new JButton("Mark Read");
        markReadBtn.setFont(Theme.SMALL_FONT);
        markReadBtn.addActionListener(e -> {
            notificationDAO.markAsRead(n.getId());
            loadNotifications();
        });
        card.add(markReadBtn, BorderLayout.EAST);

        return card;
    }
}
