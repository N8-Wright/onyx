package neat.http;

public class HttpRequestParserException extends RuntimeException
{
    public HttpRequestParserException(String errorMessage)
    {
        super(errorMessage);
    }
}
