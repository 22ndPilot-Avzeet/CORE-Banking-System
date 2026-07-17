package auth;
import dao.DBConnection;
import exceptions.InvalidCredentialsException;

import java.sql.*;


public class account_exists
{
    public static boolean exists;
    static Connection con;

    static {
        try {
            con = DBConnection.getConnectionAcc();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

   public static boolean account_check(String an) throws Throwable
    {
        try
        {
            CallableStatement cstmt = con.prepareCall("{call checkAccountExists(?, ?)}");
            cstmt.setString(1, an);
            cstmt.registerOutParameter(2, java.sql.Types.BOOLEAN);
            cstmt.execute();
            exists = cstmt.getBoolean(2);
            if(!exists) {
                //throw new InvalidCredentialsException("Account does not exists");
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
