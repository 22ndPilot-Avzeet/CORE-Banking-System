package service;
import dao.DBConnection;
import auth.account_exists;
import java.sql.*;
import java.util.*;

public class LoanService
{
    double loan_amount;
    double salary;
    int age;
    double credit_score,income;
    boolean loan_status;
    boolean acc_status;
    double avg_balance;//for 6 months
    Scanner Sc=new Scanner(System.in);
    static Connection con;
    static {
        try {
            con = DBConnection.getConnectionAcc();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public LoanService(double salary, int age, double credit_score, double income, boolean loan_status, boolean acc_status, double avg_balance) {
        this.salary = salary;
        this.age = age;
        this.credit_score = credit_score;
        this.income = income;
        this.loan_status = loan_status;
        this.acc_status = acc_status;
        this.avg_balance = avg_balance;
    }


    LoanService info(String an) throws Exception
    {
        /*String query = "SELECT salary, age, credit_score, monthly_income, loan_status, status, avg_balance FROM customer_details WHERE account_number=?";

        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, an);

        ResultSet rs = ps.executeQuery();

        if(rs.next())
        {
            return new LoanService(
                    rs.getDouble("salary"),
                    rs.getInt("age"),
                    rs.getDouble("credit_score"),
                    rs.getDouble("monthly_income"),
                    rs.getBoolean("loan_status"),
                    rs.getString("status").equalsIgnoreCase("ACTIVE"),
                    rs.getDouble("avg_balance")
            );
        }*/

        return null;
    }

    void loan() throws Throwable
    {
        try
        {
            System.out.println("Enter account number");
            String an=Sc.next();
            if(account_exists.account_check(an))            {
                if (loan_condition(an)) {
                    System.out.println("loan approved");
                    System.out.println("enter loan amount");
                    loan_amount = Sc.nextDouble();
                    if(loan_amount <= 0)
                    {
                        System.out.println("Invalid loan amount.");
                        return;
                    }
                    if(loan_amount > salary * 24)
                    {
                        System.out.println("Loan amount exceeds eligibility.");
                        return;
                    }
                    String add = "INSERT INTO LOANS(account_number, loan_amount, loan_status) VALUES(?,?,?)";
                    PreparedStatement ps = con.prepareCall(add);
                    ps.setString(1, an);
                    ps.setString(1, an);
                    ps.setDouble(2, loan_amount);
                    ps.setString(3, "ACTIVE");
                    ps.executeUpdate();
                    System.out.println();
                }
                else {
                    System.out.println("loan request declined");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    boolean loan_condition(String an) throws Exception {
        LoanService ls=info(an);
        if(ls.acc_status&&(!ls.loan_status))
        {
            return ls.credit_score >= 700
                    && ls.age >= 21
                    && ls.income >= 25000
                    && ls.avg_balance >= 20000;
        }
        else
            return false;
    }
}
