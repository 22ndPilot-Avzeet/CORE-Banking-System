package auth;
import model.*;


public class login
{
    String username,pass;

    String loginuser(String u,String p)
    {//add trial attempts
        if(Customer.username.equalsIgnoreCase(u)&& Customer.pass.equals(p))
            return "Customer";
        else if (Manager.userName.equalsIgnoreCase(u)&&Manager.pass.equals(p))
            return "Manager";
        else
            return null;
    }
    boolean logout()
    {
        return false;
    }
}
