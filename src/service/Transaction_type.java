package service;
import auth.*;
import dao.*;
import model.*;
import auth.TOTP;
import utils.BankConstants;

import java.sql.*;
import java.time.LocalDate;

public class Transaction_type
{
    public Transaction t;
   public boolean proceed=false;
    public Connection con;

    public Transaction_type(Transaction t) throws Throwable {
        this.t = t;
        this.con = DBConnection.getConnectionAcc();
        if(account_exists.account_check(t.getTo_acc())&&account_exists.account_check(t.getFrom_acc())&& t.getAmount()>0)
            proceed=true;
    }


    public void tran_update(String stat) throws Throwable
    {
        try {
            String update="Update Transactions set status=?,timestamp=? where txn_id=?";
            PreparedStatement ps3=con.prepareStatement(update);
            ps3.setString(1,stat);
            ps3.setTimestamp(2, java.sql.Timestamp.from(java.time.Instant.now()));
            ps3.setString(3,t.getTx_id());
            ps3.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void depositt() throws Throwable
    {
        String first = t.getFrom_acc().compareTo(t.getTo_acc()) < 0
                ? t.getFrom_acc()
                : t.getTo_acc();

        String second = first.equals(t.getFrom_acc())
                ? t.getTo_acc()
                : t.getFrom_acc();

        synchronized(first.intern()) {
            synchronized(second.intern()) {
                try {
                    CallableStatement cstmt = con.prepareCall("{call getBalance(?)}");
                    cstmt.setString(1, t.getTo_acc());
                    ResultSet rs = cstmt.executeQuery();
                    double balance = 0;
                    while (rs.next()) {
                        balance = rs.getDouble(1);
                    }
                    String deposit = "Update account set balance=?,updated_at=? where account_number=?;";
                    PreparedStatement ps = con.prepareStatement(deposit);
                    ps.setDouble(1, balance + t.getAmount());
                    ps.setDate(2, Date.valueOf(LocalDate.now()));
                    ps.setString(3, t.getTo_acc());
                    ps.executeUpdate();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public boolean withdraww() throws Throwable
    {
        String first = t.getFrom_acc().compareTo(t.getTo_acc()) < 0
                ? t.getFrom_acc()
                : t.getTo_acc();

        String second = first.equals(t.getFrom_acc())
                ? t.getTo_acc()
                : t.getFrom_acc();

        synchronized(first.intern()) {
            synchronized(second.intern()) {
                try {
                    CallableStatement cstmt = con.prepareCall("{call getBalance(?)}");
                    cstmt.setString(1, t.getFrom_acc());
                    ResultSet rs = cstmt.executeQuery();
                    double balance = 0;
                    while (rs.next()) {
                        balance = rs.getDouble(1);
                    }
                    if (t.getAmount() > (balance - BankConstants.min_balance)) {
                        return false;
                    } else {
                        String withdraw = "Update account set balance=?,updated_at=? where account_number=?;";
                        PreparedStatement ps = con.prepareStatement(withdraw);
                        ps.setDouble(1, balance - t.getAmount());
                        ps.setDate(2, Date.valueOf(LocalDate.now()));
                        ps.setString(3, t.getFrom_acc());
                        ps.executeUpdate();
                        return true;
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public void intra_bank() throws Throwable
    {
        con.setAutoCommit(false);
        try {
            if(proceed)
            {
                TOTP totp=new TOTP();

                if(!totp.verify(t))
                {
                    con.rollback();

                    t.setStatus("FAILED");

                    tran_update("FAILED");

                    return;
                }
                boolean b=withdraww();
                if(b) {
                    depositt();
                    tran_update("SUCCESS");
                    con.commit();
                }
                else {
                    con.rollback();
                    t.setStatus("Failed");
                    tran_update("FAILED");
                }
            }
            else
            {
                con.rollback();
                t.setStatus("Failed");
                tran_update("FAILED");
            }
        } catch (Exception e) {
            con.rollback();
            t.setStatus("Failed");
            tran_update("FAILED");
        }
        finally{
            con.setAutoCommit(true);
        }
    }
    public void deposit() throws Throwable
    {
        con.setAutoCommit(false);
        try {
            if(proceed)
            {
                depositt();
                tran_update("SUCCESS");
                con.commit();
            }
            else
            {
                con.rollback();
                t.setStatus("Failed");
                tran_update("FAILED");
            }
        } catch (Exception e) {
            con.rollback();
            t.setStatus("Failed");
            tran_update("FAILED");
        }
        finally{
            con.setAutoCommit(true);
        }
    }
    public void withdrawal() throws Throwable
    {
        con.setAutoCommit(false);
        try {
            if(proceed)
            {
                TOTP totp=new TOTP();

                if(!totp.verify(t))
                {
                    con.rollback();

                    t.setStatus("FAILED");

                    tran_update("OTP_FAILED");

                    return;
                }
                boolean b=withdraww();
                if(b) {
                    tran_update("SUCCESS");
                    con.commit();
                }
                else {
                    con.rollback();
                    t.setStatus("Failed");
                    tran_update("FAILED");
                }
            }
            else
            {
                con.rollback();
                t.setStatus("Failed");
                tran_update("FAILED");
            }
        } catch (Exception e) {
            con.rollback();
            t.setStatus("Failed");
            tran_update("FAILED");
        }
        finally{
            con.setAutoCommit(true);
        }
    }
    public void inter_bank() throws Throwable
    {
        con.setAutoCommit(false);
        try {
            if(proceed)
            {

            }
            else
            {
                con.rollback();
                t.setStatus("Failed");
                tran_update("FAILED");
            }
        } catch (Exception e) {
            con.rollback();
            t.setStatus("Failed");
            tran_update("FAILED");
        }
        finally{
            con.setAutoCommit(true);
        }
    }
}
