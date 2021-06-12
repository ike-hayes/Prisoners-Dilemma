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
    String hostIP;
    /**
     * Constructor for objects of class ClientSide
     */
    public ClientSide()
    {
        // initialise instance variables
        Scanner input=new Scanner(System.in);
        try{
            System.out.println("Enter the IP address you would like to connect to");
            hostIP=input.nextLine();
            Socket mySocket=new Socket(hostIP,3456);
            DataOutputStream output=new DataOutputStream(mySocket.getOutputStream());
        }catch(Exception e){
            System.out.println("Something went wrong connecting to that host");
        }
    }
}
