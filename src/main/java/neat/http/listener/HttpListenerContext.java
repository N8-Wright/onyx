package neat.http.listener;

public class HttpListenerContext
{
    public HttpListenerContext(HttpListenerRequest request, HttpListenerResponse response)
    {
        Request = request;
        Response = response;
    }

    public HttpListenerRequest Request;

    public HttpListenerResponse Response;
}
