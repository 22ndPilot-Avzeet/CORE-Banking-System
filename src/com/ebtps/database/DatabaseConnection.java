package com.ebtps.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton class to manage the database connection.
 * Demonstrates Singleton pattern and JDBC Connectivity.
 */
public class DatabaseConnection {
    // Database configuration - Connected to Supabase
    private static final String URL = "jdbc:postgresql://db.myrmpevjekpszfwtulck.supabase.co:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "CoreBankingSystem@123";

    // The single instance
    private static DatabaseConnection instance;

    // Private constructor to prevent instantiation from outside
    private DatabaseConnection() {
        try {
            // Load the PostgreSQL driver
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load PostgreSQL driver: " + e.getMessage());
        }
    }

    /**
     * Provides the global point of access to the DatabaseConnection instance.
     * Uses textbook synchronized method for thread safety.
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Returns a new active database connection.
     * Since DAOs use try-with-resources, this must return a fresh connection
     * to avoid closing a shared singleton connection.
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
