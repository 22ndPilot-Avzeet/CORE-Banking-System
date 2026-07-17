package exceptions;

public class DailyLimitExceededException extends Throwable
{
    public DailyLimitExceededException(String mess)
    {
        System.out.println(mess);
    }
}
