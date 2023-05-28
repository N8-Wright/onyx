package neat.http.listener;

import neat.http.constants.HttpMethod;
import neat.http.constants.HttpVersion;
import neat.util.ByteArray;

import java.util.HashMap;

public class HttpListenerRequest
{
    public HttpListenerRequest(
            HttpMethod method,
            HttpVersion version,
            byte[] url,
            HashMap<ByteArray, byte[]> headers,
            ByteArray rawMessage) {
        Method = method;
        Version = version;
        Url = url;
        Headers = headers;
        RawMessage = rawMessage;
    }

    public final HttpMethod Method;
    public final HttpVersion Version;
    public final byte[] Url;
    public final HashMap<ByteArray, byte[]> Headers;
    public final ByteArray RawMessage;
}
