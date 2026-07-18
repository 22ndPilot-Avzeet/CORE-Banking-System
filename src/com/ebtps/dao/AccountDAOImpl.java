package com.ebtps.dao;

import com.ebtps.database.DatabaseConnection;
import com.ebtps.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of AccountDAO.
 * Interacts with the PostgreSQL database.
 */
public class AccountDAOImpl implements AccountDAO {

    @Override
    public List<Account> getAccountsByUserId(int userId) {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT account_number, user_id, account_type, balance, status, daily_transfer_limit " +
                       "FROM Accounts WHERE user_id = ?";
                       
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Account account = new Account(
                        rs.getString("account_number"),
                        rs.getInt("user_id"),
                        rs.getString("account_type"),
                        rs.getBigDecimal("balance"),
                        com.ebtps.model.AccountStatus.valueOf(rs.getString("status").toUpperCase()),
                        rs.getBigDecimal("daily_transfer_limit")
                    );
                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error retrieving accounts: " + e.getMessage());
        }
        return accounts;
    }
}
