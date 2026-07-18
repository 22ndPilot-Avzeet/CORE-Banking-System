package com.ebtps.dao;

import com.ebtps.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportDAOImpl implements ReportDAO {

    @Override
    public Map<String, Double> getCustomerExpenseAnalytics(int userId) {
        Map<String, Double> expenses = new HashMap<>();
        // Sum withdrawals and transfers out of the user's accounts, grouped by category
        String query = "SELECT t.category, SUM(t.amount) as total " +
                       "FROM Transactions t " +
                       "JOIN Accounts a ON t.source_account = a.account_number " +
                       "WHERE a.user_id = ? AND t.transaction_type IN ('WITHDRAWAL', 'TRANSFER') AND t.status = 'SUCCESS' " +
                       "GROUP BY t.category";
                       
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    expenses.put(rs.getString("category"), rs.getDouble("total"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching expense analytics: " + e.getMessage());
        }
        return expenses;
    }

    @Override
    public Map<String, String> getManagerSystemReport() {
        Map<String, String> report = new LinkedHashMap<>(); // Maintain order
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection()) {
            
            // 1. Total Active Accounts
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM Accounts WHERE status = 'ACTIVE'")) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) report.put("Total Active Accounts", String.valueOf(rs.getInt(1)));
                }
            }
            
            // 2. Total Frozen Accounts
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM Accounts WHERE status = 'FROZEN'")) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) report.put("Total Frozen Accounts", String.valueOf(rs.getInt(1)));
                }
            }

            // 3. Total Transaction Volume (Amount)
            try (PreparedStatement ps = conn.prepareStatement("SELECT SUM(amount) FROM Transactions WHERE status = 'SUCCESS'")) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) report.put("Total Txn Volume (₹)", String.format("%.2f", rs.getDouble(1)));
                }
            }
            
            // 4. Pending Loans Count
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM Loans WHERE status = 'PENDING'")) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) report.put("Pending Loans", String.valueOf(rs.getInt(1)));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching system report: " + e.getMessage());
        }
        
        return report;
    }
}
