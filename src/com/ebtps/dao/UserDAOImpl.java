package com.ebtps.dao;

import com.ebtps.database.DatabaseConnection;
import com.ebtps.exceptions.InvalidCredentialsException;
import com.ebtps.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of UserDAO.
 * Handles actual database interaction for User-related operations.
 */
public class UserDAOImpl implements UserDAO {

    @Override
    public User authenticate(String username, String password) throws InvalidCredentialsException {
        // Query to check username and password
        String query = "SELECT id, username, full_name, email, phone, role FROM Users WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password); // In a real app, hash the input password before comparing
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        com.ebtps.model.Role.valueOf(rs.getString("role").toUpperCase())
                    );
                } else {
                    throw new InvalidCredentialsException("Invalid username or password.");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Database error during authentication: " + e.getMessage());
            // Optionally log this error internally
            throw new InvalidCredentialsException("An error occurred during authentication. Please try again.");
        }
    }
}
