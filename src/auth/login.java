package auth;
import model.*;


public class login
{
    String username,pass;

    String loginuser(String u,String p)
    {//add trial attempts
        if(Employee.username.equalsIgnoreCase(u)&& Employee.pass.equals(p))
            return "Employee";
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
