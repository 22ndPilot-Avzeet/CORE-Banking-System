package queue;

import dao.DBConnection;
import java.io.*;
import java.sql.*;
import java.util.*;

public class AccountDetails {

    public static void updateAccounts() {

        try {

            Connection con = DBConnection.getConnectionAcc();

            String query = "SELECT * FROM ACCOUNTS";

            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            // Collection 1
            ArrayList<String> accountList = new ArrayList<>();

            while (rs.next()) {

                String row =
                        rs.getString("account_number") + ","
                                + rs.getInt("account_id") + ","
                                + rs.getString("account_type") + ","
                                + rs.getDouble("balance") + ","
                                + rs.getString("status");

                accountList.add(row);
            }

            // Collection 2
            Vector<String> csvRows = new Vector<>();

            csvRows.add("Last Updated," + new Timestamp(System.currentTimeMillis()));
            csvRows.add("");
            csvRows.add("Account Number,User ID,Account Type,Balance,Status");

            csvRows.addAll(accountList);

            BufferedWriter bw = new BufferedWriter(new FileWriter("accounts.csv"));

            for (String row : csvRows) {

                bw.write(row);
                bw.newLine();

            }

            bw.close();

        }
        catch (Exception e) {

            e.printStackTrace();

        }

    }

}