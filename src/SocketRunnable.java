import java.io.*;
import  java.util.*;
import java.net.*;

public class SocketRunnable implements Runnable
{
    private Socket socket;
    private String login;

    public SocketRunnable(Socket aSocket)
    {
        socket = aSocket;
    }
    private static void sendMessageToAllClients(String message)
    {
        for(PrintWriter output: Server.listOfOutputs)
        {
            output.println(message);
        }
    }

    @Override
    public void run()
    {
        try
        {
            Scanner input = new Scanner(socket.getInputStream());
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            login = input.nextLine();
            System.out.println("client *"+login+"* connected");
            Server.listOfOutputs.add(output);
            sendMessageToAllClients(login+" joined us");
            while(true)
            {
                synchronized (this)
                {
                    String messageFromClient = input.nextLine();
                    System.out.println(login+"  sent message ["+messageFromClient+"]");
                    if(messageFromClient.equals("endOfSession"))
                    {
                        sendMessageToAllClients(login+" left us");
                        Server.listOfOutputs.remove(output);
                        input.close();
                        output.close();
                        break;
                    }

                    sendMessageToAllClients(login+": "+messageFromClient);
                }

            }
        }
        catch (Exception e)
        {
            System.out.println("Error: can't operate network I/O");
        }
        finally
        {
            try
            {
                if(socket!=null)
                {
                    System.out.println("closing socket");
                    socket.close();
                }

            }
            catch (Exception e)
            {
                System.out.println("Error: can't close socket");
            }

        }

    }
}
