package queue;

import service.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class PendingTransaction {

    public static void pending(Map<String, Transaction> qu) {

        try {

            // Collection 1
            ArrayList<Transaction> pendingList = new ArrayList<>();

            // Copy queue into ArrayList
            for (Map.Entry<String, Transaction> entry : qu.entrySet()) {

                Transaction t = entry.getValue();

                if (!t.getStatus().equalsIgnoreCase("SUCCESS")
                        && !t.getStatus().equalsIgnoreCase("FAILED")
                        && !t.getStatus().equalsIgnoreCase("FRAUD")
                        && !t.getStatus().equalsIgnoreCase("OTP_FAILED")) {

                    pendingList.add(t);

                }

            }

            // Collection 2
            Vector<String> csvRows = new Vector<>();

            csvRows.add("Last Updated," + new Timestamp(System.currentTimeMillis()));

            csvRows.add("Total Pending," + pendingList.size());

            csvRows.add("");

            csvRows.add("Transaction ID,From Account,To Account,Transaction Type,Amount,Status");

            for (Transaction t : pendingList) {

                csvRows.add(
                        t.getTx_id() + ","
                                + t.getFrom_acc() + ","
                                + t.getTo_acc() + ","
                                + t.getTx_type() + ","
                                + t.getAmount() + ","
                                + t.getStatus()
                );

            }

            BufferedWriter bw = new BufferedWriter(new FileWriter("pending_transactions.csv"));

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