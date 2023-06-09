package onyx.http.listener;

import onyx.http.constants.HttpMethod;
import onyx.http.constants.HttpVersion;
import onyx.util.ByteArray;
import onyx.util.ByteSpan;

import java.util.HashMap;

public class HttpListenerRequest
{
    public HttpListenerRequest(
            HttpMethod method,
            HttpVersion version,
            ByteSpan url,
            HashMap<ByteSpan, ByteSpan> headers,
            ByteArray rawMessage) {
        Method = method;
        Version = version;
        Url = url;
        Headers = headers;
        RawMessage = rawMessage;
    }

    public final HttpMethod Method;
    public final HttpVersion Version;
    public final ByteSpan Url;
    public final HashMap<ByteSpan, ByteSpan> Headers;
    public final ByteArray RawMessage;
}
