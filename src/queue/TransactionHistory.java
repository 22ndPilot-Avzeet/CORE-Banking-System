package queue;

import service.Transaction;
import java.io.*;
import java.sql.*;
import java.util.*;

public class TransactionHistory {

    public static void addTransaction(Transaction t, String serverName) {

        try {

            // Collection Class
            Queue<String> historyQueue = new ArrayDeque<>();

            File file = new File("transaction_history.csv");

            boolean newFile = !file.exists();

            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));

            // Header only once
            if (newFile) {

                bw.write("Transaction ID,From Account,To Account,Transaction Type,Amount,Status,Timestamp,Processed By");
                bw.newLine();

            }

            // Create CSV row
            String row =
                    t.getTx_id() + ","
                            + t.getFrom_acc() + ","
                            + t.getTo_acc() + ","
                            + t.getTx_type() + ","
                            + t.getAmount() + ","
                            + t.getStatus() + ","
                            + new Timestamp(System.currentTimeMillis()) + ","
                            + serverName;

            historyQueue.offer(row);

            while (!historyQueue.isEmpty()) {

                bw.write(historyQueue.poll());
                bw.newLine();

            }

            bw.close();

        }
        catch (Exception e) {

            e.printStackTrace();

        }

    }

}