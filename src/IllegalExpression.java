public class IllegalExpression extends Exception
{
    IllegalExpression(String message)
    {
        super(message);
    }

    @Override
    public String getMessage(){
        return super.getMessage();
    }
}
