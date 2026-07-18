package exceptions;

public class AccountNotFoundException extends Throwable
{
    public AccountNotFoundException(String mess)
    {
        System.out.println(mess);
    }
}
