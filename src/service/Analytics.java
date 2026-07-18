package service;
import model.*;
import fraud.*;
import dao.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.*;

//add  these to io file
public class Analytics
{

    public Connection con;
    {
        try {
            con = DBConnection.getConnectionAcc();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    long totaltxn(int days)
     {
         try {
             LocalDate dt2 = LocalDate.now().minusDays(days);
             long k=0;
             String  count="Select count(*) from Transactions where txn_id is not null and timestamp::date = ? ;";
             PreparedStatement ps= con.prepareCall(count);
             ps.setDate(1,Date.valueOf(dt2));
             //ps.setTimestamp(2,Timestamp.valueOf(LocalDateTime.now()));
             ResultSet rs=ps.executeQuery();

             if(rs.next())
                 k=rs.getLong(1);
             return k;

         } catch (Exception e) {
             throw new RuntimeException(e);
         }
     }

    long successfultxn(int days)
    {
        try {
            LocalDate dt2=LocalDate.now().minusDays(days);
            long k=0;
            String  count="Select count(*) from Transactions where txn_id is not null and timestamp::date = ? and status='SUCCESS';";
            PreparedStatement ps= con.prepareCall(count);
            ps.setDate(1,Date.valueOf(dt2));
            //ps.setTimestamp(2,Timestamp.valueOf(LocalDateTime.now()));
            ResultSet rs=ps.executeQuery();

            if(rs.next())
                k=rs.getLong(1);
            return k;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    long failedtxn(int days)
    {
        try {
            LocalDate dt2=LocalDate.now().minusDays(days);
            long k=0;
            String  count="Select count(*) from Transactions where txn_id is not null and timestamp::date = ? and status='FAILED';";
            PreparedStatement ps= con.prepareCall(count);
            ps.setDate(1,Date.valueOf(dt2));
            //ps.setTimestamp(2,Timestamp.valueOf(LocalDateTime.now()));
            ResultSet rs=ps.executeQuery();

            if(rs.next())
                k=rs.getLong(1);
            return k;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    long fraudtxn(int days)
    {
        try
        {
            LocalDate dt=LocalDate.now().minusDays(days);

            long k=0;

            String count="Select count(*) from Transactions where timestamp::date=? and status='FRAUD';";
            PreparedStatement ps= con.prepareStatement(count);
            ps.setDate(1,Date.valueOf(dt));
            ResultSet rs=ps.executeQuery();

            if(rs.next())
                k=rs.getLong(1);

            return k;
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    double txnAmount(int days)
    {
        try
        {
            LocalDate dt2=LocalDate.now().minusDays(days);
            double amo=0;
            String sum="Select sum(amount) from Transactions where txn_id is not null and status='SUCCESS' and timestamp::date = ?;";
            PreparedStatement ps= con.prepareCall(sum);
            ps.setDate(1,Date.valueOf(dt2));
            //ps.setTimestamp(2,Timestamp.valueOf(LocalDateTime.now()));
            ResultSet rs=ps.executeQuery();

            if(rs.next())
                amo=rs.getLong(1);
            return amo;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    int[] peaktxnHour() {
        try {
            int k = 0;
            int n = 0;
            String peak = "Select extract(hour from timestamp),count(*) from Transactions where timestamp::date=? group by extract(hour from timestamp) order by count(*) desc limit 1;";
            PreparedStatement ps = con.prepareStatement(peak);
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                k = rs.getInt(1);
                n = rs.getInt(2);
            }
            return new int[]{k, n};
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    double avgtxns(int days)
    {
        try {
            LocalDateTime dt2=LocalDateTime.now().minusDays(days);
            double k=0;
            String  count="Select count(*) from Transactions where txn_id is not null and timestamp between ? and ? ;";
            PreparedStatement ps= con.prepareCall(count);
            ps.setTimestamp(1,Timestamp.valueOf(dt2));
            ps.setTimestamp(2,Timestamp.valueOf(LocalDateTime.now()));
            ResultSet rs=ps.executeQuery();

            if(rs.next())
                k=rs.getLong(1);
            return k;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    long txntype(int days,String type)
    {
        try {
            LocalDate dt2=LocalDate.now().minusDays(days);
            long k=0;
            String  count=null;
            if(type.equalsIgnoreCase("intra_bank"))
                count="Select count(*) from Transactions where txn_id is not null and timestamp::date = ? and status='SUCCESS' and tx_type='INTRA_BANK';";
            else if(type.equalsIgnoreCase("inter_bank"))
                count="Select count(*) from Transactions where txn_id is not null, timestamp::date = ? and status='SUCCESS' and tx_type='INTER_BANK';";
            PreparedStatement ps= con.prepareCall(count);
            ps.setDate(1,Date.valueOf(dt2));
            //ps.setTimestamp(2,Timestamp.valueOf(LocalDateTime.now()));
            ResultSet rs=ps.executeQuery();

            if(rs.next())
                k=rs.getLong(1);
            return k;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    String[] peakday(int days)
    {
        try {
            String k = null;
            int n = 0;
            String peak = "Select TO_CHAR(timestamp, 'Day'),count(*) from Transactions where timestamp::date between ? and ? group by TO_CHAR(timestamp, 'Day') order by count(*) desc limit 1;";
            PreparedStatement ps = con.prepareStatement(peak);
            ps.setDate(1,Date.valueOf(LocalDate.now().minusDays(days)));
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                k = rs.getString(1);
                n = rs.getInt(2);
            }
            return new String[]{k, String.valueOf(n)};
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void daily()
    {
        try {
            Analytics anly=new Analytics();
            System.out.println("Total Transactions- "+ anly.totaltxn(0));

            System.out.println("Total successful Transactions- "+anly.successfultxn(0));

            System.out.println("Total Failed Transactions- "+ anly.failedtxn(0));

            System.out.println("Total fraud Transactions- "+anly.fraudtxn(0));

            int peak[]=anly.peaktxnHour();
            System.out.println("Peak hour- "+peak[0]+ ":00 (" +peak[1] +" Transactions)");

            System.out.println("Total Amount Processed - " +anly.txnAmount(0));

            double total=anly.totaltxn(0);
            double fraud=anly.fraudtxn(0);
            System.out.println("Fraud Rate - " +(fraud/total)*100 +"%");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void weekly()
    {
        try {
            Analytics anly=new Analytics();
            double sum=0;
            System.out.println("Daily successful Trasaction count-- ");
            for(int i=0;i<7;i++)
            {
                sum=sum+anly.successfultxn(i);
                LocalDate ld=LocalDate.now().minusDays(i);
                System.out.println("\t\t\t "+ld+" - "+anly.successfultxn(i));
            }
            System.out.println("Total transactions- "+sum);

            sum=0;
            System.out.println("\n");
            System.out.println("Daily revenue/amount processed- ");
            for(int i=0;i<7;i++)
            {
                sum=sum+anly.txnAmount(i);
                LocalDate ld=LocalDate.now().minusDays(i);
                System.out.println("\t\t\t "+ld+" - "+anly.txnAmount(i));
            }
            System.out.println("Total transaction amount- "+sum);

            sum=0;
            System.out.println("\n");
            System.out.println("Fraud trend-- ");
            for(int i=0;i<7;i++)
            {
                LocalDate ld = LocalDate.now().minusDays(i);

                System.out.println(
                        ld+" - "+anly.fraudtxn(i));
            }
            System.out.println();

            sum=0;
            System.out.println("\n");
            String p[]=anly.peakday(7);
            System.out.println("busiest day of week- "+p[0] +"- "+p[1]+" transactions");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void monthly()
    {
        try {
            Analytics anly=new Analytics();
            double sum=0;
            for(int i=0;i<30;i++)
            {
                sum=sum+anly.successfultxn(i);
            }
            System.out.println("Total transactions- "+sum);

            sum=0;
            System.out.println("\n");
            for(int i=0;i<30;i++)
            {
                sum=sum+anly.txnAmount(i);
            }
            System.out.println("Total amount- "+sum);

            sum=0;
            System.out.println("\n");
            System.out.println("Fraud trend-- ");
            for(int i=0;i<30;i++)
            {
                LocalDate ld = LocalDate.now().minusDays(i);

                System.out.println(
                        ld+" - "+anly.fraudtxn(i));
            }
            System.out.println();

            sum=0;
            double sum2=0;
            System.out.println("\n");
            for(int i=0;i<30;i++)
            {
                sum=sum+anly.successfultxn(i);
                sum2=sum2+anly.totaltxn(i);
            }
            System.out.println("Success rate-- "+ (sum/sum2)*100);

            sum=0;
            sum2=0;
            System.out.println("\n");
            for(int i=0;i<30;i++)
            {
                sum=sum+anly.failedtxn(i);
                sum2=sum2+anly.totaltxn(i);
            }
            System.out.println("Failure rate-- "+ (sum/sum2)*100);

            sum=0;
            sum2=0;
            System.out.println("\n");
            for(int i=0;i<30;i++)
            {
                sum=sum+anly.fraudtxn(i);
                sum2=sum2+anly.totaltxn(i);
            }
            System.out.println("Fraud rate-- "+ (sum/sum2)*100);

            sum=0;
            sum2=0;
            System.out.println("\n");
            System.out.println("Avg transactions in this month- "+(anly.avgtxns(30)/30));

            System.out.println("\n");
            System.out.println("Total inter bank transactions- "+anly.txntype(30,"inter_bank"));

            System.out.println("\n");
            System.out.println("Total intra bank transactions- "+anly.txntype(30,"intra_bank"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
