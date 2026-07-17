package exceptions;

public class AccountClosedException extends Throwable
{
    public AccountClosedException(String mess)
    {
        System.out.println(mess);
    }
}
