package exceptions;

public class UnauthorizedAccessException extends Throwable
{
    public UnauthorizedAccessException(String mess)
    {
        System.out.println(mess);
    }
}
