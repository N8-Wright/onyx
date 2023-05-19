package neat.http;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

public class HttpRequestParser
{
    static final int MaxMethodLength = 7;
    static final Map<String, HttpMethod> HttpMethods = Map.ofEntries(
            entry("CONNECT", HttpMethod.Connect),
            entry("DELETE", HttpMethod.Delete),
            entry("GET", HttpMethod.Get),
            entry("HEAD", HttpMethod.Head),
            entry("OPTIONS", HttpMethod.Options),
            entry("PATCH", HttpMethod.Patch),
            entry("POST", HttpMethod.Post),
            entry("PUT", HttpMethod.Put),
            entry("TRACE", HttpMethod.Trace));

    private int _index;
    private String _message;

    public HttpRequestParser(String httpMessage)
    {
        _index = 0;
        _message = httpMessage;
    }

    public HttpRequestParserResult parse()
    {
        var method = parseMethod();
        return new HttpRequestParserResult();
    }

    private HttpMethod parseMethod()
    {
        int startIndex = _index;
        while (!atEOF() && !atWhitespace())
        {
            if (_index - startIndex > MaxMethodLength)
            {
                throw new HttpRequestParserException("Exceeded maximum method length");
            }

            _index++;
        }

        var method = _message.substring(startIndex, _index);
        return HttpMethods.get(method);
    }

    private boolean atEOF()
    {
        return _index >= _message.length();
    }

    private boolean atWhitespace()
    {
        return _message.charAt(_index) == ' ';
    }
}
