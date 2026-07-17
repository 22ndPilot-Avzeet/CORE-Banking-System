package exceptions;

public class AccountFrozenException extends Throwable
{
    public AccountFrozenException(String mess)
    {
        System.out.println(mess);
    }
}
