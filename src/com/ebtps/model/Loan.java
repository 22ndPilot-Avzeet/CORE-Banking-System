package com.ebtps.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Represents a Loan request in the EBTPS application.
 */
public class Loan {
    private final int id;
    private final int userId;
    private final BigDecimal amount;
    private final BigDecimal interestRate;
    private final int durationMonths;
    private final String status; // PENDING, APPROVED, REJECTED, PAID
    private final Timestamp requestDate;
    private final Integer managerId;

    public Loan(int id, int userId, BigDecimal amount, BigDecimal interestRate, 
                int durationMonths, String status, Timestamp requestDate, Integer managerId) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.interestRate = interestRate;
        this.durationMonths = durationMonths;
        this.status = status;
        this.requestDate = requestDate;
        this.managerId = managerId;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getInterestRate() { return interestRate; }
    public int getDurationMonths() { return durationMonths; }
    public String getStatus() { return status; }
    public Timestamp getRequestDate() { return requestDate; }
    public Integer getManagerId() { return managerId; }
}
