package service;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import exceptions.InsufficientBalanceException;
import model.Customer;
import utils.*;
import auth.account_exists;
import model.Account;
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
        try
        {
            System.out.println("enter userId");
            int id=Sc.nextInt();
            System.out.println("enter Account number");
            String an=Sc.next();
            while (true)
            {
                System.out.println("enter deposited amount");
                double c=Sc.nextDouble();
                if(c<BankConstants.min_balance)
                    throw new InsufficientBalanceException("Minimum initial amount should be 5000..try again");
                else
                    break;
            }
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
            String delete="delete from account where account_number=?";
            PreparedStatement ps= con.prepareCall(delete);
            ps.setString(1,an);
            int n=ps.executeUpdate();
            if(n>0)
                System.out.println("Account closed");
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
            String deposit="Update account set balance=?,updated_at=? where account_number=?;";
            PreparedStatement ps=con.prepareCall(deposit);
            ps.setDouble(1,balance+amo);
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ps.setString(3,an);
            ps.executeUpdate();
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
                String withdraw="Update account set balance=?,updated_at=? where account_number=?;";
                PreparedStatement ps=con.prepareCall(withdraw);
                ps.setDouble(1,amo);
                ps.setDate(2, Date.valueOf(LocalDate.now()));
                ps.setString(3,an);
                ps.executeUpdate();
                System.out.println("amount withdrawed successfully");
            }
            } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
