package com.ebtps.model;

/**
 * Represents a User in the EBTPS application.
 * Demonstrates encapsulation.
 */
public class User {
    private final int id;
    private final String username;
    private final String fullName;
    private final String email;
    private final String phone;
    private final Role role;

    public User(int id, String username, String fullName, String email, String phone, Role role) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Role getRole() {
        return role;
    }
}
