package fraud;

import dao.*;
import utils.*;
import service.*;
import java.sql.*;

public class FraudDetection
{
    Transaction t;
    Connection con;
    String reason = "";

    public FraudDetection(Transaction t) throws Exception
    {
        this.t=t;
        con=DBConnection.getConnectionAcc();
    }

    public boolean checkFraud() throws Exception
    {
        if(amountCheck()) {
            reason = "Large Amount Transaction";
            return true;
        }

        if(frequencyCheck()) {
            reason = "High Transaction Frequency";
            return true;
        }

        if(dailyLimitCheck()) {
            reason = "Daily Transfer Limit Exceeded";
            return true;
        }

        if(accountStatusCheck()) {
            reason = "Account Frozen/Closed";
            return true;
        }

        if(selfTransferCheck()) {
            reason = "Self Transfer Detected";
            return true;
        }

        return false;
    }

    public boolean amountCheck()
    {
        return t.getAmount()>BankConstants.max_fraud_amount;
    }

    public boolean frequencyCheck() throws Exception
    {
        String sql="SELECT COUNT(*) FROM TRANSACTIONS WHERE from_account=? AND timestamp>=NOW()-INTERVAL '1 minute';";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1,t.getFrom_acc());
        ResultSet rs=ps.executeQuery();
        rs.next();
        return rs.getInt(1)>=BankConstants.max_txn_per_minute;
    }

    public boolean dailyLimitCheck() throws Exception
    {
        //String sql="SELECT COALESCE(SUM(amount),0) FROM Transactions WHERE from_account=? AND DATE(timestamp)=CURRENT_DATE AND status='SUCCESS';";
        String sql="SELECT SUM(amount) FROM ( SELECT amount FROM TRANSACTIONS WHERE from_account=? AND DATE(timestamp)=CURRENT_DATE AND status='SUCCESS' UNION ALL SELECT 0) AS combined_data;";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1,t.getFrom_acc());
        ResultSet rs=ps.executeQuery();
        rs.next();
        double total=rs.getDouble(1);
        return total+t.getAmount()>BankConstants.max_daily_transfer;
    }

    public boolean accountStatusCheck() throws Exception
    {
        String sql="SELECT status FROM ACCOUNTS WHERE account_number=?;";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1,t.getFrom_acc());
        ResultSet rs=ps.executeQuery();
        if(rs.next())
        {
            String status=rs.getString(1);

            if(status.equalsIgnoreCase("ACTIVE"))
                return false;
            else
                return true;
        }

        return true;
    }

    public boolean selfTransferCheck()
    {
        return t.getFrom_acc().equals(t.getTo_acc());
    }

    public String getReason()
    {
        return reason;
    }
}