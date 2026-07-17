package exceptions;



public class InsufficientBalanceException extends Throwable
{
    public InsufficientBalanceException(String mess)
    {
        System.out.println(mess);
    }
}


