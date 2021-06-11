/**
 * Write a description of class ServerSide here.
 *
 * @author Ike Hayes
 * @version 11/6/21
 */
import java.io.*;
import java.net.*;
import java.net.InetAddress;
public class ServerSide
{
    // instance variables - replace the example below with your own
    /**
     * Constructor for objects of class NetworkTesting
     */
    public ServerSide()
    {
        // initialise instance variables
        try{
            ServerSocket listen=new ServerSocket(3456);
            Socket mySocket=listen.accept();
            DataInputStream input=new DataInputStream(mySocket.getInputStream());
            String message = input.readUTF();
            System.out.println(message);
            listen.close();
        } catch(Exception e){
            e.printStackTrace();
        }     
    }
}
