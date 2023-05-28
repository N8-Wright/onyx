package neat.http;

import neat.http.constants.HttpMethod;
import neat.http.constants.HttpVersion;
import neat.http.parser.HttpRequestParser;
import neat.http.parser.HttpRequestParserEOFException;
import neat.util.ByteArray;
import neat.util.ByteSpan;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;
import java.util.stream.Stream;

class HttpRequestParserTest
{
    @ParameterizedTest
    @ValueSource(strings = {"CONNECT", "DELETE", "GET", "HEAD", "OPTIONS", "PATH", "POST", "PUT", "TRACE"})
    public void parseMethod_NoWhitespaceAtEnd_ExceptionThrown(String method)
    {
        var parser = new HttpRequestParser(ByteArray.of(method));
        Assertions.assertThrows(HttpRequestParserEOFException.class, parser::parse);
    }

    @Test
    public void parseRequest_MissingSecondNewline_ExceptionThrown()
    {
        var request = "GET /helloWorld HTTP/1.0\r\n";
        var parser = new HttpRequestParser(ByteArray.of(request));
        Assertions.assertThrows(HttpRequestParserEOFException.class, parser::parse);
    }

    @ParameterizedTest
    @MethodSource("httpMethodSource")
    public void parseRequest_ValidRequest_ResultsReturned(String method, HttpMethod expectedMethod)
    {
        var request = ByteArray.of(String.format("%s / HTTP/1.1\r\nTest: Value\r\n\r\n", method));
        var parser = new HttpRequestParser(request);
        var result = parser.parse();

        Assertions.assertEquals(expectedMethod, result.Method);
        Assertions.assertEquals(ByteSpan.of("/"), result.Url);
        Assertions.assertEquals(HttpVersion.HTTP11, result.Version);

        var expectedHeaders = Map.ofEntries(Map.entry(ByteSpan.of("Test"), ByteSpan.of("Value")));
        Assertions.assertEquals(expectedHeaders.keySet(), result.Headers.keySet());

        for (var key : expectedHeaders.keySet())
        {
            var expectedValue = expectedHeaders.get(key);
            var value = result.Headers.get(key);

            Assertions.assertEquals(expectedValue, value);
        }
    }

    public static Stream<Arguments> httpMethodSource()
    {
        return Stream.of(
                Arguments.arguments("CONNECT", HttpMethod.Connect),
                Arguments.arguments("DELETE", HttpMethod.Delete),
                Arguments.arguments("GET", HttpMethod.Get),
                Arguments.arguments("HEAD", HttpMethod.Head),
                Arguments.arguments("OPTIONS", HttpMethod.Options),
                Arguments.arguments("PATCH", HttpMethod.Patch),
                Arguments.arguments("POST", HttpMethod.Post),
                Arguments.arguments("PUT", HttpMethod.Put),
                Arguments.arguments("TRACE", HttpMethod.Trace)
        );
    }
}