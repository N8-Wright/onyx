package neat.http;

import java.util.Map;

import static java.util.Map.entry;

public class HttpRequestParser
{
    static final int MaxMethodLength = 7;
    static final int MaxRequestTargetLength = 512;
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
        var requestTarget = parseRequestTarget();

        return new HttpRequestParserResult();
    }

    private HttpMethod parseMethod()
    {
        int startIndex = _index;
        while (!atWhitespace())
        {
            if (atEOF())
            {
                throw new HttpRequestParserException("Unexpected EOF");
            }

            if (_index - startIndex > MaxMethodLength)
            {
                throw new HttpRequestParserException("Exceeded maximum method length");
            }

            _index++;
        }

        var method = _message.substring(startIndex, _index);
        skipWhitespace();
        return HttpMethods.get(method);
    }

    private String parseRequestTarget()
    {
        int startIndex = _index;
        while (!atWhitespace())
        {
            if (atEOF())
            {
                throw new HttpRequestParserException("Unexpected EOF");
            }

            if (_index - startIndex > MaxRequestTargetLength)
            {
                throw new HttpRequestParserException("Exceeded maximum method length");
            }

            _index++;
        }

        var requestTarget = _message.substring(startIndex, _index);
        skipWhitespace();
        return requestTarget;
    }

    private boolean atEOF()
    {
        return _index >= _message.length();
    }

    private boolean atWhitespace()
    {
        return _message.charAt(_index) == ' ';
    }

    private void skipWhitespace()
    {
        while (atWhitespace())
        {
            _index++;
        }
    }
}
