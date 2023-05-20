package neat.http.listener;

import neat.http.constants.HttpMethod;
import neat.http.constants.HttpVersion;

import java.util.HashMap;

public class HttpListenerRequest
{
    public HttpListenerRequest(
            HttpMethod method,
            HttpVersion version,
            String url,
            HashMap<String, String> headers,
            String rawMessage) {
        Method = method;
        Version = version;
        Url = url;
        Headers = headers;
        RawMessage = rawMessage;
    }

    public final HttpMethod Method;
    public final HttpVersion Version;
    public final String Url;
    public final HashMap<String, String> Headers;
    public final String RawMessage;
}
