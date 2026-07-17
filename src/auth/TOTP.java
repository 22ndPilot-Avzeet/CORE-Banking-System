package auth;
import service.*;
import javax.swing.*;
import java.util.Random;
import utils.*;

public class TOTP
{
    String otp;
    long generatedTime;

    public boolean verify(Transaction t)
    {
        if(t.getAmount()<=BankConstants.totp_limit)
            return true;

        Random r=new Random();

        otp=String.valueOf(100000+r.nextInt(900000));

        generatedTime=System.currentTimeMillis();

        JOptionPane.showMessageDialog(
                null,
                "******** TOTP GENERATED ********\n\n"
                        +"Transaction ID : "+t.getTx_id()
                        +"\nOTP : "+otp
                        +"\n\nValid for 30 Seconds",
                "Transaction Verification",
                JOptionPane.INFORMATION_MESSAGE
        );

        String enteredOTP=
                JOptionPane.showInputDialog(
                        null,
                        "Enter OTP"
                );

        if(enteredOTP==null)
            return false;

        long currentTime=System.currentTimeMillis();

        if(currentTime-generatedTime>30000)
        {
            JOptionPane.showMessageDialog(
                    null,
                    "OTP Expired"
            );

            return false;
        }

        if(!otp.equals(enteredOTP))
        {
            JOptionPane.showMessageDialog(
                    null,
                    "Incorrect OTP"
            );

            return false;
        }

        return true;
    }

}