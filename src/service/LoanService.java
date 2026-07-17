package service;
import dao.DBConnection;
import auth.account_exists;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
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


    LoanService info(String an)
    {
        return new LoanService(0,0,0,0,false,false,0);
    }


    void loan()
    {
        try
        {
            System.out.println("Enter account number");
            String an=Sc.next();
            if(account_exists.exists)
            {
                if (loan_condition(an)) {
                    System.out.println("loan approved");
                    System.out.println("enter loan amount");
                    loan_amount = Sc.nextDouble();
                    String add = "insert into loan values(?,?,?)";
                    PreparedStatement ps = con.prepareCall(add);
                    ps.setString(1, an);
                    //add others values
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

    boolean loan_condition(String an)
    {
        LoanService ls=info(an);
        if(ls.acc_status&&(!ls.loan_status))
        {
            if(ls.credit_score>=700&&ls.age>=21&&ls.income>=25000&&ls.avg_balance>=20000)
                return true;
            else
                return false;
        }
        else
            return false;
    }
}
