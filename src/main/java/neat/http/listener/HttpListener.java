package neat.http.listener;

import neat.http.listener.HttpListenerContext;
import neat.http.listener.HttpListenerRequest;
import neat.http.listener.HttpListenerResponse;
import neat.http.parser.HttpRequestParser;

import java.io.IOException;
import java.net.*;

public class HttpListener
{
    private ServerSocket _serverSocket;

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
        parser.parse();
        return new HttpListenerContext(new HttpListenerRequest(httpMessage), new HttpListenerResponse());
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

