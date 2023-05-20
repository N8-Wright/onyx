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

    public HttpMethod Method;
    public HttpVersion Version;
    public String Url;
    public HashMap<String, String> Headers;
}
