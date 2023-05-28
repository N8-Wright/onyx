package onyx.http.parser;

import onyx.http.constants.HttpMethod;
import onyx.http.constants.HttpVersion;
import onyx.util.ByteSpan;

import java.util.HashMap;

public class HttpRequestParserResult
{

    public HttpRequestParserResult(HttpMethod method, HttpVersion version, ByteSpan url, HashMap<ByteSpan, ByteSpan> headers)
    {
        Method = method;
        Version = version;
        Url = url;
        Headers = headers;
    }

    public final HttpMethod Method;
    public final HttpVersion Version;
    public final ByteSpan Url;
    public final HashMap<ByteSpan, ByteSpan> Headers;
}
