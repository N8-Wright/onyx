package neat.http.listener;

import neat.http.constants.HttpMethod;
import neat.http.parser.HttpRequestParser;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class HttpListener
{
    private final ServerSocket _serverSocket;

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

    private byte[] receiveExtraContent(Socket sock, HttpMethod method, HashMap<String, String> headers) throws IOException
    {
        switch (method)
        {
            case Post:
            case Put:
                var contentType = headers.get("Content-Type");
                var contentLengthStr = headers.get("Content-Length");
                if (contentType == null)
                {
                    throw new HeaderNotFoundException("Content-Type");
                }

                if (contentLengthStr == null)
                {
                    throw new HeaderNotFoundException("Content-Length");
                }

                var contentLength = Integer.parseInt(contentLengthStr);
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

    private String receiveHttpMessage(Socket sock) throws IOException
    {
        var stream = sock.getInputStream();
        var message = new StringBuilder();
        var buffer = new byte[1024];

        while (true)
        {
            int bytesRead = stream.read(buffer);

            message.ensureCapacity(message.length() + bytesRead);
            for (int i = 0; i < bytesRead; i++)
            {
                message.append((char)buffer[i]);
            }

            var length = message.length();
            if (message.charAt(length - 4) == '\r' &&
                    message.charAt(length - 3) == '\n' &&
                    message.charAt(length - 2) == '\r' &&
                    message.charAt(length - 1) == '\n')
            {
                break;
            }
        }

        return message.toString();
    }
}

