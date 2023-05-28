package neat.http.listener;

import neat.http.constants.HttpMethod;
import neat.http.parser.HttpRequestParser;
import neat.util.ByteArray;
import neat.util.ByteSpan;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class HttpListener
{
    private final ServerSocket _serverSocket;
    private final ByteArray _contentType = ByteArray.of("Content-Type");
    private final ByteArray _contentLength = ByteArray.of("Content-Length");

    public HttpListener(int port) throws IOException
    {
        _serverSocket = new ServerSocket(port);
    }

    public void close() throws IOException
    {
        _serverSocket.close();
    }

    public HttpListenerContext receive() throws IOException
    {
        var client = _serverSocket.accept();
        var httpMessage = receiveHttpMessage(client);
        var parser = new HttpRequestParser(httpMessage);
        var result = parser.parse();
        try
        {
            byte[] content = receiveExtraContent(client, result.Method, result.Headers);
        }
        catch (HeaderNotFoundException e)
        {
            // TODO: return a specific response
        }
        catch (Exception e)
        {
            // TODO: return a specific response
        }

        return new HttpListenerContext(
                new HttpListenerRequest(
                        result.Method,
                        result.Version,
                        result.Url,
                        result.Headers,
                        httpMessage),
                new HttpListenerResponse());
    }

    private byte[] receiveExtraContent(Socket sock, HttpMethod method, HashMap<ByteSpan, ByteSpan> headers) throws IOException
    {
        switch (method)
        {
            case Post:
            case Put:
                var contentType = headers.get(_contentType);
                var contentLengthSpan = headers.get(_contentLength);
                if (contentType == null)
                {
                    throw new HeaderNotFoundException("Content-Type");
                }

                if (contentLengthSpan == null)
                {
                    throw new HeaderNotFoundException("Content-Length");
                }

                var contentLength = contentLengthSpan.getInt();
                if (contentLength >= 0 && contentLength < 16_000_000) // 16 megabytes
                {
                    throw new IOException("Invalid Content-Length");
                }

                var content = new byte[contentLength];
                var inputStream = sock.getInputStream();
                var bytesRead = inputStream.read(content);
                if (bytesRead != contentLength)
                {
                    throw new IOException("Unable to read entirety of expected content");
                }

                return content;
            default:
                return null;
        }
    }

    private ByteArray receiveHttpMessage(Socket sock) throws IOException
    {
        var stream = sock.getInputStream();
        var message = new ByteArray(1024);
        var buffer = new byte[1024];

        while (true)
        {
            int bytesRead = stream.read(buffer);
            message.push(buffer, bytesRead);

            var length = message.length();
            if (message.get(length - 4) == '\r' &&
                    message.get(length - 3) == '\n' &&
                    message.get(length - 2) == '\r' &&
                    message.get(length - 1) == '\n')
            {
                break;
            }
        }

        return message;
    }
}

