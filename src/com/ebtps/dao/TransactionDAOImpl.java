package com.ebtps.dao;

import com.ebtps.database.DatabaseConnection;
import com.ebtps.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {

    @Override
    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT id, reference_number, source_account, destination_account, amount, " +
                       "transaction_type, status, category, description, created_at " +
                       "FROM Transactions WHERE source_account = ? OR destination_account = ? " +
                       "ORDER BY created_at DESC LIMIT 100";
                       
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, accountNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaction tx = new Transaction(
                        rs.getInt("id"),
                        rs.getString("reference_number"),
                        rs.getString("source_account"),
                        rs.getString("destination_account"),
                        rs.getBigDecimal("amount"),
                        rs.getString("transaction_type"),
                        rs.getString("status"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at")
                    );
                    transactions.add(tx);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching transactions: " + e.getMessage());
        }
        return transactions;
    }
}
