import java.io.PrintWriter;
import  java.util.*;
import java.net.*;

public class Server
{
    private final static int PORT = 3444;
    static ArrayList<PrintWriter> listOfOutputs;

    static
    {
        listOfOutputs = new ArrayList<>();
    }

    public static void main(String[] args)
    {
        ServerSocket serverSocket = null;
        try
        {
            serverSocket = new ServerSocket(PORT);
            while (true)
            {
                Socket socket = serverSocket.accept();
                new Thread(new SocketRunnable(socket)).start();
            }
        }
        catch (Exception e)
        {
            System.out.println("Error: Can't create socket on port "+PORT);
            System.exit(1);
        }
        finally
        {
            try
            {
                if(serverSocket!=null)
                {
                    System.out.println("Closing serverSocket");
                    serverSocket.close();
                }
            }
            catch (Exception e)
            {
                System.out.println("Error: can't close serverSocket");
            }
        }
    }
}
