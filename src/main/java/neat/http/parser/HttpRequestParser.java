package neat.http.parser;

import neat.http.constants.HttpMethod;
import neat.http.constants.HttpVersion;
import neat.util.ByteArray;

import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

public class HttpRequestParser
{
    static final int MaxMethodLength = 7;
    static final int MaxRequestTargetLength = 512;
    static final int MaxVersionLength = 8;
    static final Map<ByteArray, HttpMethod> HttpMethods = Map.ofEntries(
            entry(ByteArray.of("CONNECT"), HttpMethod.Connect),
            entry(ByteArray.of("DELETE"), HttpMethod.Delete),
            entry(ByteArray.of("GET"), HttpMethod.Get),
            entry(ByteArray.of("HEAD"), HttpMethod.Head),
            entry(ByteArray.of("OPTIONS"), HttpMethod.Options),
            entry(ByteArray.of("PATCH"), HttpMethod.Patch),
            entry(ByteArray.of("POST"), HttpMethod.Post),
            entry(ByteArray.of("PUT"), HttpMethod.Put),
            entry(ByteArray.of("TRACE"), HttpMethod.Trace));

    static final Map<ByteArray, HttpVersion> HttpVersions = Map.ofEntries(
            entry(ByteArray.of("HTTP/1.0"), HttpVersion.HTTP1),
            entry(ByteArray.of("HTTP/1.1"), HttpVersion.HTTP11),
            entry(ByteArray.of("HTTP/2.0"), HttpVersion.HTTP2));

    private int _index;
    private final ByteArray _message;

    public HttpRequestParser(ByteArray httpMessage)
    {
        _index = 0;
        _message = httpMessage;
    }

    public HttpRequestParserResult parse()
    {
        var method = parseMethod();
        var requestTarget = parseRequestTarget();
        var version = parseVersion();
        var headers = parseHeaders();

        return new HttpRequestParserResult(method, version, requestTarget, headers);
    }

    private HttpMethod parseMethod()
    {
        int startIndex = _index;
        while (true)
        {
            if (atEOF())
            {
                throw new HttpRequestParserEOFException();
            }

            if (atWhitespace())
            {
                break;
            }

            if (_index - startIndex > MaxMethodLength)
            {
                throw new HttpRequestParserException("Exceeded maximum method length");
            }

            _index++;
        }

        var buffer = ByteArray.of(_message.slice(startIndex, _index));
        skipWhitespace();
        return HttpMethods.get(buffer);
    }

    private byte[] parseRequestTarget()
    {
        int startIndex = _index;
        while (!atWhitespace())
        {
            if (atEOF())
            {
                throw new HttpRequestParserEOFException();
            }

            if (_index - startIndex > MaxRequestTargetLength)
            {
                throw new HttpRequestParserException("Exceeded maximum method length");
            }

            _index++;
        }

        var requestTarget = _message.slice(startIndex, _index);
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
                throw new HttpRequestParserEOFException();
            }

            if (_index - startIndex > MaxVersionLength)
            {
                throw new HttpRequestParserException("Exceeded maximum method length");
            }

            if (atNewline())
            {
                break;
            }

            _index++;
        }

        var version = ByteArray.of(_message.slice(startIndex, _index));
        skipNewline();
        return HttpVersions.get(version);
    }

    private HashMap<ByteArray, byte[]> parseHeaders()
    {
        var headers = new HashMap<ByteArray, byte[]>();
        while (true)
        {
            if (_index + 1 >= _message.length())
            {
                throw new HttpRequestParserEOFException();
            }

            if (atNewline())
            {
                break;
            }

            headers.put(ByteArray.of(parseHeaderKey()), parseHeaderValue());
        }

        skipNewline();
        return headers;
    }

    private byte[] parseHeaderKey()
    {
        int startIndex = _index;
        while (!atEOF() && _message.get(_index) != ':')
        {
            _index++;
        }

        var key = _message.slice(startIndex, _index);
        _index++; // Skip over ':'

        if (atEOF())
        {
            throw new HttpRequestParserEOFException();
        }

        if (_message.get(_index) != ' ')
        {
            throw new HttpRequestParserException("Expected space preceding header value");
        }

        _index++; // Skip over ' '
        return key;
    }

    private byte[] parseHeaderValue()
    {
        int startIndex = _index;
        while (true)
        {
            if (_index + 1 >= _message.length())
            {
                throw new HttpRequestParserEOFException();
            }

            if (atNewline())
            {
                break;
            }

            _index++;
        }

        var value = _message.slice(startIndex, _index);
        skipNewline();
        return value;
    }

    private boolean atEOF()
    {
        return _index >= _message.length();
    }

    private boolean atWhitespace()
    {
        return _message.get(_index) == ' ';
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

    private boolean atNewline()
    {
        return _message.get(_index) == '\r' && _message.get(_index + 1) == '\n';
    }
}
