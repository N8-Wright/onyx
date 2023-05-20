package neat.http.listener;

public class HeaderNotFoundException extends RuntimeException
{
    public HeaderNotFoundException(String header)
    {
        ExpectedHeader = header;
    }

    public final String ExpectedHeader;
}
