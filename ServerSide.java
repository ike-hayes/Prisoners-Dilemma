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
    ServerSocket thread=null;
    final int MAXPLAYERS=2;
    final int PORT=3456;
    int roundScores[]={0,0};  
    String playerName;  
    Socket sockets[]=new Socket[MAXPLAYERS]; 
    String usernames[]=new String[MAXPLAYERS]; 
    DataInputStream[] streamIn=new DataInputStream[MAXPLAYERS];
    DataOutputStream[] streamOut=new DataOutputStream[MAXPLAYERS];
    int connectedClients=0;
    boolean readyToPlay=false;
    // instance variables - replace the example below with your own
    /**
     * Constructor for objects of class NetworkTesting
     */
    public ServerSide()
    {
        // initialise instance variables
        try{
            System.out.println("Your IP is "+InetAddress.getLocalHost().getHostAddress()+" tell your friend to connect here!");
            server=new ServerSocket(PORT);
        } catch(Exception e){
            System.out.println("Something went wrong hosting the server");
        }  
        try{
            boolean playersJoining=true;
            while (playersJoining){ 
                System.out.println("Waiting for players");
                socket=server.accept();
                System.out.println("Talking on port: "+socket.getLocalPort()+" and to port: " +socket.getPort());
                                    
                DataOutputStream say=new DataOutputStream(socket.getOutputStream());
                DataInputStream clientSays = new DataInputStream(socket.getInputStream());
                String theySaid=(String)clientSays.readUTF();
                playerName=theySaid;

                try {
                    thread=new ServerSocket(0);
                    System.out.println("Handing off to port: "+thread.getLocalPort());
                } catch (Exception e){
                    System.out.println("Failed to create handoff port");
                    System.out.println(e);
                }
                
                say.writeUTF(thread.getLocalPort()+"");
                say.close();
                clientSays.close();
                socket.close();
                socket=thread.accept();

                say=new DataOutputStream(socket.getOutputStream());
                clientSays = new DataInputStream(socket.getInputStream());
                say.flush();

                addGameSession(socket,clientSays,say);

                if (readyToPlay) playMatch();
            }
        } catch (Exception e){
            System.out.println("Error during setup");
            System.out.println(e);
            try { 
                server.close(); 
            } catch (Exception e1){
                System.out.println("Couldn't close the server");
            }
        }
        try {
            socket.close();
        }catch (Exception e){
            System.out.println("Couldn't close the socket");
        }
    }
    void addGameSession(Socket s, DataInputStream theySaid, DataOutputStream say){    
        sockets[connectedClients]=s;
        usernames[connectedClients]=playerName;
        streamOut[connectedClients]=say;
        streamIn[connectedClients]=theySaid;
        connectedClients++;
        System.out.println(usernames[connectedClients-1]+" has joined successfully!");
        if (connectedClients==MAXPLAYERS) readyToPlay=true;                    
    }
    void updateScore(int player, String playerMove, String OpponentMove){
        if (playerMove.equals("SILENT") && OpponentMove.equals("SILENT"))
            roundScores[player]+=2;
        else if (playerMove.equals("SILENT") && OpponentMove.equals("SNITCH"))
            roundScores[player]+=5;
        else if (playerMove.equals("SNITCH") && OpponentMove.equals("SNITCH"))
            roundScores[player]+=4;
        else if (playerMove.equals("SNITCH") && OpponentMove.equals("SILENT"))
            roundScores[player]+=0;
        else  System.out.println("Recieved incorrect choices");
    }
    void playMatch(){
        DataInputStream theysay;
        DataOutputStream Isay;
        String choices[] = new String[MAXPLAYERS];
        System.out.println("Starting game with "+usernames[0]+" and "+usernames[1]);
        int roundsToPlay=20;
        for (int i=0;i<MAXPLAYERS;i++){
            System.out.println("Telling "+usernames[i]+" game has started");
            try {
                streamOut[i].writeUTF("Game has started!");
            } catch (Exception e){
                System.out.println("Couldn't start the game");
            }                
        }
        for (int round=1;round<=roundsToPlay;round++){
            for (int i=0;i< MAXPLAYERS;i++){
                System.out.println("Listening for "+usernames[i]+" move.  ");
                try{
                    choices[i]=streamIn[i].readUTF().toUpperCase();
                    System.out.println(usernames[i]+" has chosen "+choices[i]);
                } catch (Exception e){
                    System.out.println("Couldn't recieve player move");
                }
            }       
            if (round<roundsToPlay){
                for (int i=0;i<MAXPLAYERS;i++){
                    System.out.println("Sharing other players move to "+usernames[i]);
                    try {
                        streamOut[i].writeUTF(choices[opponent(i)]);
                        updateScore(i,choices[i],choices[opponent(i)]);
                    } catch (Exception e){
                        System.out.println("Couldn't share opponent choice with "+usernames[i]);
                    }
                }
            }
            if(round<roundsToPlay){
                for (int i=0;i<MAXPLAYERS;i++){
                    System.out.println("Sharing scores to "+usernames[i]);
                    try{
                        streamOut[i].writeUTF(Integer.toString(roundScores[i]));
                        streamOut[i].writeUTF(Integer.toString(roundScores[opponent(i)]));
                    }catch(Exception e){
                        System.out.println("Couldn't share scores with "+usernames[i]);
                    }
                }
            }
        }  
        for (int i=0;i< MAXPLAYERS;i++){           
            updateScore(i,choices[i],choices[opponent(i)]);
            try {                  
                streamOut[i].writeUTF("Game over!");
            } catch (Exception e){
                System.out.println("Couldn't send end message to "+usernames[i]);
            }
        }
        for (int i=0;i<MAXPLAYERS;i++){
            System.out.println("Sending "+usernames[i]+" their score");
            try{
                streamOut[i].writeUTF(Integer.toString(roundScores[i]));
            }catch(Exception e){
                System.out.println("Couldn't share scores with "+usernames[i]);
            }
        }
        for (int i=0;i<MAXPLAYERS;i++){
            System.out.println("Shutting down connections to "+usernames[i]);
            try {
                streamOut[i].close();
                streamIn[i].close();
                connectedClients--;
            } catch (Exception e){
                System.out.println("Couldn't close connection with "+usernames[i]);
            }
        }
    }
    int opponent(int player){
        return(MAXPLAYERS-player-1);
    }
}