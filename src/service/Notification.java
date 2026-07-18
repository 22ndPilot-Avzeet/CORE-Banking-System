package service;

import javax.swing.JOptionPane;

public class Notification {

    public void fraudAlert(Transaction t, String reason)
    {
        JOptionPane.showMessageDialog(
                null,
                "FRAUD DETECTED\n\n"
                        + "Transaction ID : " + t.getTx_id()
                        + "\nFrom Account : " + t.getFrom_acc()
                        + "\nTo Account : " + t.getTo_acc()
                        + "\nAmount : ₹" + t.getAmount()
                        + "\nReason : " + reason,
                "Fraud Alert",
                JOptionPane.WARNING_MESSAGE
        );
    }
}