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
    Socket socket=null;
    ServerSocket server=null;
    DataInputStream streamIn=null;
    // instance variables - replace the example below with your own
    /**
     * Constructor for objects of class NetworkTesting
     */
    public ServerSide()
    {
        // initialise instance variables
        try{
            System.out.println("Your IP is "+InetAddress.getLocalHost().getHostAddress()+". Tell your friend to connect here!");
            server=new ServerSocket(3456);
        } catch(Exception e){
            System.out.println("Something went wrong hosting the server");
        }  
        accept();
    }
    public void accept(){
        while(true){
            try{
                socket=server.accept();
                streamIn=new DataInputStream(socket.getInputStream());
            }catch(Exception e){
                System.out.println("Something went wrong connecting");
            }
        }
    }
}
