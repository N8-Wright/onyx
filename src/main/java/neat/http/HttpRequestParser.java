package neat.http;

import java.util.Map;

import static java.util.Map.entry;

public class HttpRequestParser
{
    static final int MaxMethodLength = 7;
    static final int MaxRequestTargetLength = 512;
    static final int MaxVersionLength = 8;
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

    static final Map<String, HttpVersion> HttpVersions = Map.ofEntries(
            entry("HTTP/1.0", HttpVersion.HTTP1),
            entry("HTTP/1.1", HttpVersion.HTTP11),
            entry("HTTP/2.0", HttpVersion.HTTP2));

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
        var version = parseVersion();

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

    private HttpVersion parseVersion()
    {
        int startIndex = _index;
        while (true)
        {
            if (_index + 1 >= _message.length())
            {
                throw new HttpRequestParserException("Unexpected EOF");
            }

            if (_index - startIndex > MaxVersionLength)
            {
                throw new HttpRequestParserException("Exceeded maximum method length");
            }

            if (_message.charAt(_index) == '\r' && _message.charAt(_index + 1) == '\n')
            {
                break;
            }

            _index++;
        }

        var version = _message.substring(startIndex, _index);
        skipNewline();
        return HttpVersions.get(version);
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

    private void skipNewline()
    {
        _index += 2;
    }
}
