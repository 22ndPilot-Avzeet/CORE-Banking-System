package exceptions;

public class DublicateBeneficiaryException extends Throwable
{
    public DublicateBeneficiaryException(String mess)
    {
        System.out.println(mess);
    }
}
