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
    /**
     * Constructor for objects of class ClientSide
     */
    public ClientSide()
    {
        // initialise instance variables
        Scanner kb=new Scanner(System.in);
        try{
            Socket mySocket=new Socket("localhost",3456);
            DataOutputStream output=new DataOutputStream(mySocket.getOutputStream());
            String message=kb.nextLine();
            System.out.println(message);
            output.writeUTF(message);
            output.flush();
            output.close();
            mySocket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
