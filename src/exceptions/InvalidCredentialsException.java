package exceptions;

public class InvalidCredentialsException extends Throwable
{
    public InvalidCredentialsException(String mess)
    {
        System.out.println(mess);
    }
}
