package model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Scanner;

public class Account
{
    int userid;
    String accountNumber;
    String accountHolderName;
    double currentBalance;
    double transferAmount;
    String status;
    LocalDateTime createdAt;
    double loanamount;
    double interestRate;
    long dailyTransferlimit;

    public Account(int userid,String accountNumber, double currentBalance) {
        this.userid = userid;
        this.accountNumber = accountNumber;
        this.currentBalance = currentBalance;
    }

    public Account(int userid,String accountNumber, double currentBalance, double transferAmount) {
        this.accountNumber = accountNumber;
        this.currentBalance = currentBalance;
        this.transferAmount = transferAmount;
    }

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Account(String accountNumber, double currentBalance, double loanamount, double interestRate) {
        this.accountNumber = accountNumber;
        this.currentBalance = currentBalance;
        this.loanamount = loanamount;
        this.interestRate = interestRate;
    }
}
