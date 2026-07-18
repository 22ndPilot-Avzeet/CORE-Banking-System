package com.ebtps.dao;

import com.ebtps.database.DatabaseConnection;
import com.ebtps.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerDAOImpl implements ManagerDAO {

    @Override
    public Account getAccountByNumber(String accountNumber) {
        String query = "SELECT account_number, user_id, account_type, balance, status, daily_transfer_limit " +
                       "FROM Accounts WHERE account_number = ?";
                       
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, accountNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Account(
                        rs.getString("account_number"),
                        rs.getInt("user_id"),
                        rs.getString("account_type"),
                        rs.getBigDecimal("balance"),
                        com.ebtps.model.AccountStatus.valueOf(rs.getString("status").toUpperCase()),
                        rs.getBigDecimal("daily_transfer_limit")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching account: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean updateAccountStatus(String accountNumber, String status) {
        String query = "UPDATE Accounts SET status = ? WHERE account_number = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, status);
            pstmt.setString(2, accountNumber);
            
            int affected = pstmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating account status: " + e.getMessage());
            return false;
        }
    }
}
