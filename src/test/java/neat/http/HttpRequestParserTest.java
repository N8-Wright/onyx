package neat.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class HttpRequestParserTest
{
    @ParameterizedTest
    @ValueSource(strings = {"CONNECT", "DELETE", "GET", "HEAD", "OPTIONS", "PATH", "POST", "PUT", "TRACE"})
    public void parseMethod_NoWhitespaceAtEnd_ExceptionThrown(String method)
    {
        var parser = new HttpRequestParser(method);
        Assertions.assertThrows(HttpRequestParseEOFException.class, () ->
        {
            parser.parse();
        });
    }
}