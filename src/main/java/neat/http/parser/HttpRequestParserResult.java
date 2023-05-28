package neat.http.parser;

import neat.http.constants.HttpMethod;
import neat.http.constants.HttpVersion;
import neat.util.ByteArray;

import java.util.HashMap;

public class HttpRequestParserResult
{

    public HttpRequestParserResult(HttpMethod method, HttpVersion version, byte[] url, HashMap<ByteArray, byte[]> headers)
    {
        Method = method;
        Version = version;
        Url = url;
        Headers = headers;
    }

    public final HttpMethod Method;
    public final HttpVersion Version;
    public final byte[] Url;
    public final HashMap<ByteArray, byte[]> Headers;
}
