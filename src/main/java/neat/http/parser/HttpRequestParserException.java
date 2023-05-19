package neat.http.parser;

public class HttpRequestParserException extends RuntimeException
{
    public HttpRequestParserException(String errorMessage)
    {
        super(errorMessage);
    }
}
