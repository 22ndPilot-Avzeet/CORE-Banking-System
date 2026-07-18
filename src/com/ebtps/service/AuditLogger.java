package com.ebtps.service;

import com.ebtps.dao.ReportDAO;
import com.ebtps.dao.ReportDAOImpl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Background daemon thread that periodically writes system audit logs to a file.
 * Demonstrates Threading and Java File I/O.
 */
public class AuditLogger implements Runnable {

    private final ReportDAO reportDAO;
    private final String logFilePath;

    public AuditLogger() {
        this.reportDAO = new ReportDAOImpl();
        this.logFilePath = "audit.log"; // Written to the root of the project directory
    }

    @Override
    public void run() {
        System.out.println("AuditLogger daemon thread started.");
        
        while (true) {
            try {
                // Sleep for 60 seconds
                Thread.sleep(60000);

                // Fetch current statistics
                Map<String, String> report = reportDAO.getManagerSystemReport();
                
                // Write to file
                writeLog(report);
                
            } catch (InterruptedException e) {
                System.out.println("AuditLogger daemon interrupted. Shutting down.");
                Thread.currentThread().interrupt(); // Restore interrupted status
                break;
            } catch (Exception e) {
                System.err.println("AuditLogger encountered an error: " + e.getMessage());
            }
        }
    }

    private void writeLog(Map<String, String> report) {
        // try-with-resources for File I/O
        // 'true' parameter in FileWriter enables append mode
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
            writer.write("--- Audit Log: " + LocalDateTime.now() + " ---");
            writer.newLine();
            
            for (Map.Entry<String, String> entry : report.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }
            writer.newLine();
            writer.flush();
            System.out.println("AuditLogger wrote entry to " + logFilePath);
        } catch (IOException e) {
            System.err.println("AuditLogger failed to write to file: " + e.getMessage());
        }
    }
}
