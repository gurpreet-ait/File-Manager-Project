package ServerSide;
import java.net.*;

public class Server implements Runnable{
    ServerSocket port;
    Thread t;
    boolean flag;
    Server(int p) throws Exception
    {
        port = new ServerSocket(p);
        flag=true;
        t = new Thread(this);
        t.start();
    }

    public void run()
    {
        try {
            port.setSoTimeout(5000);
            while(flag)
            {
                acceptConnection();
            }
            port.close();
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("Err :" + e);
        }
    }

    void acceptConnection()
    {
        try {
            System.out.println("Waiting for client connection...");
            Socket s = port.accept();
            System.out.println("... Get a client Connection");
            new ClientProcessor(s);
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}
