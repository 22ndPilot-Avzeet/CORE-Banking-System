package service;

import java.lang.Thread;
import server.*;
import dao.*;
import java.sql.*;
import java.util.*;
import queue.*;

public class Transaction extends Thread
{
    public static LinkedHashMap<String,Transaction> qu=new LinkedHashMap<>();
    String tx_id,from_acc,to_acc,tx_type,status,processedBy;
    double amount;
    Timestamp timestamp;


    static Connection con;

    static {
        try {
            con = DBConnection.getConnectionAcc();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getStatus() {
        return status;
    }

    public String getTx_type() {
        return tx_type;
    }

    public String getFrom_acc() {
        return from_acc;
    }

    public String getTo_acc() {
        return to_acc;
    }

    public double getAmount() {
        return amount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Transaction(){};


    public Transaction(String tx_id, String from_acc, String to_acc, String tx_type, String status, double amount, Timestamp timestamp) {
        this.tx_id = tx_id;
        this.from_acc = from_acc;
        this.to_acc = to_acc;
        this.tx_type = tx_type;
        this.status = status;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getTx_id() {
        return tx_id;
    }

    public void run() {
    try {
        while (true) {

            synchronized (qu) {
                if (qu.isEmpty()) {
                    break;
                }
            }

            if (Thread.currentThread().getName().equals("m1")) {
                ServerA.serA();
            }
            else if (Thread.currentThread().getName().equals("m2")) {
                ServerB.serB();
            }
            Thread.sleep(100);
            synchronized (qu) {
                PendingTransaction.pending(qu);
            }

        }
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

    public static void main()
    {
        try {
            Transaction m1=new Transaction();
            Transaction m2=new Transaction();
            int i=0;
            CallableStatement cstmt=con.prepareCall("{call getTransactionData()}");//no argument as whole table *
            ResultSet rs=cstmt.executeQuery();

            while(rs.next())
            {
                if(!rs.getString(5).equalsIgnoreCase("SUCCESS")) {
                   qu.put(rs.getString(1),new Transaction(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getDouble(6),rs.getTimestamp(7)));
                  // i++;
                }
            }
            m1.setName("m1");
            m2.setName("m2");
            m1.start();
            m2.start();
            m1.join();
            m2.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
