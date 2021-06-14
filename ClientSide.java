/**
 * Write a description of class ClientSide here.
 *
 * @author Ike Hayes
 * @version 11/6/21
 */
import java.io.*;
import java.net.*;
import java.net.InetAddress;
import java.util.Scanner;
public class ClientSide
{
    // instance variables - replace the example below with your own
    Socket socket=null;
    DataOutputStream streamOut=null;
    /**
     * Constructor for objects of class ClientSide
     */
    public ClientSide(String hostIP)
    {
        // initialise instance variables
        Scanner input=new Scanner(System.in);
        try{
            socket=new Socket(hostIP,3456);
            streamOut=new DataOutputStream(socket.getOutputStream());
        }catch(Exception e){
            System.out.println("Something went wrong connecting to that host");
        }
    }
}
