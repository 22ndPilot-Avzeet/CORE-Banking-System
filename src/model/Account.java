package model;


public abstract class Account
{
    int userid;
    String accountNumber;
    double currentBalance;
    double transferAmount;
    double loanamount;
    double interestRate;

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
