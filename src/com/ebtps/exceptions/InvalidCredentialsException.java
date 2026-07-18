package com.ebtps.exceptions;

/**
 * Exception thrown when a user attempts to log in with incorrect credentials.
 * Demonstrates custom exception handling.
 */
public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
