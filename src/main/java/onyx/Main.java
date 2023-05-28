package onyx;

import onyx.http.listener.HttpListener;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            var listener = new HttpListener(8080);
            listener.receive();
            listener.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
