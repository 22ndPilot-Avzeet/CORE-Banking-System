package exceptions;


public class TransactionFailedException extends Throwable
{
    public TransactionFailedException(String mess)
    {
        System.out.println(mess);
    }
}
