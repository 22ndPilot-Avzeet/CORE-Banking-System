package com.ebtps.dao;

import com.ebtps.exceptions.InvalidCredentialsException;
import com.ebtps.model.User;

/**
 * Data Access Object Interface for User operations.
 * Demonstrates Abstraction.
 */
public interface UserDAO {
    /**
     * Authenticates a user against the database.
     * @param username The username provided.
     * @param password The password provided.
     * @return The authenticated User object.
     * @throws InvalidCredentialsException If authentication fails.
     */
    User authenticate(String username, String password) throws InvalidCredentialsException;
}
