package service;


import java.sql.Date;
import java.time.*;
import java.util.*;
import queue.*;
import exceptions.*;
import model.*;
import utils.*;
import dao.DBConnection;
import java.sql.*;
public class AccountService
{
    Scanner Sc=new Scanner(System.in);
    static Connection con;

    static {
        try {
            con = DBConnection.getConnectionAcc();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void createAccount() throws Throwable
    {
        try {
            System.out.println("enter userId");
            int id = Sc.nextInt();
            System.out.println("enter Account number");
            String an = Sc.next();
            System.out.println("is it a saving account or current accont(1/2)");
            int a = Sc.nextInt();
            double c=0;
            while (true) {
                System.out.println("enter deposited amount");
                c = Sc.nextDouble();
                if (c < BankConstants.min_balance)
                    throw new InsufficientBalanceException("Minimum initial amount should be 5000..try again");
                else
                    break;
            }
            String add = "insert into  ACCOUNTS(account_id,account_number,account_type,balance,status) values(?,?,?,?,'ACTIVE')";
            PreparedStatement ps = con.prepareStatement(add);
            ps.setInt(1, id);
            ps.setString(2, an);
            if (a == 1)
                ps.setString(3, "SAVING");
            else
                ps.setString(3, "CURRENT");
            ps.setDouble(4, c);
            ps.executeUpdate();
            AccountDetails.updateAccounts();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    void closeAccount() throws Exception
    {
        try
        {
            System.out.println("Enter account number");
            String an=Sc.next();
            String delete="update  ACCOUNTS set status='CLOSED' and updated_at=? where account_number=?";
            PreparedStatement ps= con.prepareCall(delete);
            ps.setTimestamp(1,Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(2,an);
            int n=ps.executeUpdate();
            if(n>0) {
                System.out.println("Account closed");
                AccountDetails.updateAccounts();
            }
            else
                System.out.println("Unsucessfull");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    void depositMoney() throws Throwable
    {
        try {
            System.out.println("Enter account number");
            String an=Sc.next();
            System.out.println("Enter amount to deposit");
            double amo=Sc.nextDouble();
            CallableStatement cstmt=con.prepareCall("{call getBalance(?)}");
            cstmt.setString(1, an);
            ResultSet rs= cstmt.executeQuery();
            double balance=0;
            while (rs.next()) {
                balance = rs.getDouble(1);
            }
            String deposit="Update  ACCOUNTS set balance=?,updated_at=? where account_number=?;";
            PreparedStatement ps=con.prepareCall(deposit);
            ps.setDouble(1,balance+amo);
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ps.setString(3,an);
            ps.executeUpdate();
            AccountDetails.updateAccounts();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void withdrawMoney() throws Throwable
    {
        try {
            System.out.println("Enter account number");
            String an=Sc.next();
            CallableStatement cstmt=con.prepareCall("{call getBalance(?)}");
            cstmt.setString(1, an);
            ResultSet rs= cstmt.executeQuery();
            System.out.println("Enter amount to deposit");
            double amo=Sc.nextDouble();
            double balance=0;
            while (rs.next()) {
                balance = rs.getDouble(1);
            }
            if(amo>(balance-BankConstants.min_balance))
            {
                throw new InsufficientBalanceException("Insufficient balance");
            }
            else {
                balance=balance-amo;
                String withdraw="Update  ACCOUNTS set balance=?,updated_at=? where account_number=?;";
                PreparedStatement ps=con.prepareCall(withdraw);
                ps.setDouble(1,balance);
                ps.setDate(2, Date.valueOf(LocalDate.now()));
                ps.setString(3,an);
                ps.executeUpdate();
                AccountDetails.updateAccounts();
                System.out.println("amount withdrawed successfully");
            }
            } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
