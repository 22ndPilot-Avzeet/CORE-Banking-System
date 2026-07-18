package com.ebtps.exceptions;

public class AccountClosedException extends Exception {
    public AccountClosedException(String message) {
        super(message);
    }
}
