package neat.http.parser;

import neat.http.constants.HttpMethod;
import neat.http.constants.HttpVersion;

import java.util.HashMap;

public class HttpRequestParserResult
{

    public HttpRequestParserResult(HttpMethod method, HttpVersion version, String url, HashMap<String, String> headers)
    {
        Method = method;
        Version = version;
        Url = url;
        Headers = headers;
    }

    public final HttpMethod Method;
    public final HttpVersion Version;
    public final String Url;
    public final HashMap<String, String> Headers;
}
