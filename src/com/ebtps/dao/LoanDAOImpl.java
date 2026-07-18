package com.ebtps.dao;

import com.ebtps.database.DatabaseConnection;
import com.ebtps.model.Loan;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class LoanDAOImpl implements LoanDAO {

    @Override
    public boolean requestLoan(int userId, BigDecimal amount, BigDecimal interestRate, int durationMonths) {
        String query = "INSERT INTO Loans (user_id, amount, interest_rate, duration_months, status) VALUES (?, ?, ?, ?, 'PENDING')";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            pstmt.setBigDecimal(2, amount);
            pstmt.setBigDecimal(3, interestRate);
            pstmt.setInt(4, durationMonths);
            
            int affected = pstmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Error requesting loan: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Loan> getLoansByUserId(int userId) {
        return fetchLoans("SELECT * FROM Loans WHERE user_id = ? ORDER BY request_date DESC", userId);
    }

    @Override
    public List<Loan> getPendingLoans() {
        // No parameter needed, but we can reuse fetchLoans with a small modification or write a specific one.
        return fetchLoans("SELECT * FROM Loans WHERE status = 'PENDING' ORDER BY request_date ASC", null);
    }

    @Override
    public boolean updateLoanStatus(int loanId, String status, int managerId) {
        String query = "UPDATE Loans SET status = ?, manager_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, managerId);
            pstmt.setInt(3, loanId);
            
            int affected = pstmt.executeUpdate();
            
            if (affected > 0) {
                // Send Notification (Asynchronously)
                int targetUserId = -1;
                try (PreparedStatement psUser = conn.prepareStatement("SELECT user_id FROM Loans WHERE id = ?")) {
                    psUser.setInt(1, loanId);
                    try (ResultSet rs = psUser.executeQuery()) {
                        if (rs.next()) targetUserId = rs.getInt("user_id");
                    }
                }
                if (targetUserId != -1) {
                    String message = "Your loan request (ID: " + loanId + ") was " + status + ".";
                    com.ebtps.service.NotificationService.getInstance().sendNotificationAsync(targetUserId, message);
                }
            }
            
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating loan status: " + e.getMessage());
            return false;
        }
    }

    private List<Loan> fetchLoans(String query, Integer param) {
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            if (param != null) {
                pstmt.setInt(1, param);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int managerId = rs.getInt("manager_id");
                    Integer mId = rs.wasNull() ? null : managerId;
                    
                    Loan loan = new Loan(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getBigDecimal("amount"),
                        rs.getBigDecimal("interest_rate"),
                        rs.getInt("duration_months"),
                        rs.getString("status"),
                        rs.getTimestamp("request_date"),
                        mId
                    );
                    loans.add(loan);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching loans: " + e.getMessage());
        }
        return loans;
    }
}
