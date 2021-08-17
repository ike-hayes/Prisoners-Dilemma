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
    //creating variables used for the server
    /**
     * Constructor for objects of class NetworkTesting
     */
    public ServerSide()
    {
        try{
            System.out.println("Your IP is "+InetAddress.getLocalHost().getHostAddress()+", tell your friend to connect here!");
            System.out.println("If you wish to play on this computer, you will need to open a client");
            server=new ServerSocket(PORT);
            /* It is a bit clunky, but for somemone to host and play
            * a game on the same computer they need to open another
            * instance to run the client.
            */
        } catch(Exception e){
            System.out.println("Something went wrong hosting the server");
        }
        //creates the server and tells the player their IP for others to connect to
        try{
            boolean playersJoining=true;
            while (playersJoining){ 
                System.out.println("Waiting for players");
                socket=server.accept();
                System.out.println("Talking on port: "+socket.getLocalPort()+" and to port: " +socket.getPort());
                //when a client connects it creates an intial input and output stream with them                    
                DataOutputStream say=new DataOutputStream(socket.getOutputStream());
                DataInputStream clientSays = new DataInputStream(socket.getInputStream());
                String usernameInput=(String)clientSays.readUTF();
                playerName=usernameInput;
                // this intial connection is used to find the players username
                try {
                    thread=new ServerSocket(0);
                    System.out.println("Handing off to port: "+thread.getLocalPort());
                } catch (Exception e){
                    System.out.println("Failed to create handoff port");
                    System.out.println(e);
                } 
                say.writeUTF(thread.getLocalPort()+"");
                //the server then sends the player the handoff port to connect to
                say.close();
                clientSays.close();
                socket.close();
                socket=thread.accept();
                //after closing all the connections the server connects to the client again on the new port
                say=new DataOutputStream(socket.getOutputStream());
                clientSays=new DataInputStream(socket.getInputStream());
                say.flush();

                addGameSession(socket,clientSays,say);
                //finally the player is ready to play and their information is added to the list of players
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
            //if something goes wrong, the server is closed.
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
        /*when a player joins, their name is stored along with the port they 
         * are talking on, and input and output streams. This is used to differentiate
         * between players and send messages to individuals.
         */
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
        //updates the scores for the players after each round
    }
    void playMatch(){
        readyToPlay=false;
        String choices[] = new String[MAXPLAYERS];
        System.out.println("Starting game with "+usernames[0]+" and "+usernames[1]);
        int roundsToPlay=5;
        //the game starts. Currently, the players play 5 rounds. This can be changed easily.
        for (int i=0;i<MAXPLAYERS;i++){
            System.out.println("Telling "+usernames[i]+" game has started");
            try {
                streamOut[i].writeUTF("Game has started!");
            } catch (Exception e){
                System.out.println("Couldn't start the game");
            }                
        }
        /* these for loops are used all throughout the gameplay section because
         * the server has to talk to each player individually. This one sends
         * a message to each client telling them the game has started
         */
        for (int round=1;round<=roundsToPlay;round++){
            for (int i=0;i< MAXPLAYERS;i++){
                System.out.println("Waiting for "+usernames[i]+"'s move.  ");
                try{
                    choices[i]=streamIn[i].readUTF().toUpperCase();
                    System.out.println(usernames[i]+" has chosen "+choices[i]);
                } catch (Exception e){
                    System.out.println("Couldn't recieve "+usernames[i]+" move");
                }
            }
            //the server receives each players choice of move
            if (round<roundsToPlay){
                for (int i=0;i<MAXPLAYERS;i++){
                    System.out.println("Sharing other players move to "+usernames[i]);
                    try {
                        streamOut[i].writeUTF(choices[opponent(i)]);
                        updateScore(i,choices[i],choices[opponent(i)]);
                    } catch (Exception e){
                        System.out.println("Couldn't share opponent's choice with "+usernames[i]);
                    }
                }
            }
            //then the score is updated and the server tells each player what the other chose
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
            //finally, the server will tell each player the current scores
        }
        //this loop continues running, with each run through entailing one round of play
        for (int i=0;i< MAXPLAYERS;i++){           
            updateScore(i,choices[i],choices[opponent(i)]);
            System.out.println("Sending "+usernames[i]+" game over message");
            try {                  
                streamOut[i].writeUTF("Game over!");
            } catch (Exception e){
                System.out.println("Couldn't send end message to "+usernames[i]);
            }
        }
        //after all the rounds have been played the scores are updated for a final time
        for (int i=0;i<MAXPLAYERS;i++){
            System.out.println("Sending "+usernames[i]+" their final score");
            try{
                streamOut[i].writeUTF(Integer.toString(roundScores[i]));
                streamOut[i].writeUTF(Integer.toString(roundScores[opponent(i)]));
                streamOut[i].writeUTF(Integer.toString(roundsToPlay));
            }catch(Exception e){
                System.out.println("Couldn't send final scores to "+usernames[i]);
            }
        }
        //each player is then sent the final scores along with how many rounds they played
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
        //finally, the server closes the connections with each player. The server will continue to run
    }
    int opponent(int player){
        return(MAXPLAYERS-player-1);
        //this method only works when there are two players. If the player is number 0 it will return 1 and vice versa
    }
}