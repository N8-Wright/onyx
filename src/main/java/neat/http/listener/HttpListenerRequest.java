package neat.http.listener;

public class HttpListenerRequest
{
    public HttpListenerRequest(String httpMessage)
    {
        RawMessage = httpMessage;
    }

    public String RawMessage;
}
