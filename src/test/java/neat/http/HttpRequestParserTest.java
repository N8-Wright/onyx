package neat.http;

import neat.http.parser.HttpRequestParserEOFException;
import neat.http.parser.HttpRequestParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class HttpRequestParserTest
{
    @ParameterizedTest
    @ValueSource(strings = {"CONNECT", "DELETE", "GET", "HEAD", "OPTIONS", "PATH", "POST", "PUT", "TRACE"})
    public void parseMethod_NoWhitespaceAtEnd_ExceptionThrown(String method)
    {
        var parser = new HttpRequestParser(method);
        Assertions.assertThrows(HttpRequestParserEOFException.class, () ->
        {
            parser.parse();
        });
    }

    @Test
    public void parseRequest_MissingSecondNewline_ExceptionThrown()
    {
        var request = "GET /helloWorld HTTP/1.0\r\n";
        var parser = new HttpRequestParser(request);
        Assertions.assertThrows(HttpRequestParserEOFException.class, () ->
        {
            parser.parse();
        });
    }
}