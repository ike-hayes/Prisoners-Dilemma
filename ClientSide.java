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
    File playerScore=new File("onlinescore.csv"); 
    Scanner input=new Scanner(System.in);
    String scoreLine[];
    String roundsPlayedLine[];
    String username;
    String usernameLine[];
    double oppScore;
    double score;
    double totalScore;
    double roundsPlayed;
    double totalRoundsPlayed;
    double gameplayRating;
    String action;
    boolean actionChosen;
    boolean playing=false;
    
    Socket socket=null;
    DataOutputStream streamOut=null;
    DataInputStream streamIn=null;
    final int PORT=3456;
    String incoming;
    int handOff;
    
    //variables used for the client side
    /**
     * Constructor for objects of class ClientSide
     */
    public ClientSide(String IP)
    {
        accessScores();
        //the scores are accessed here to find the players username 
        try{
            socket=new Socket(IP,PORT);
            streamOut=new DataOutputStream(socket.getOutputStream());
            streamIn=new DataInputStream(socket.getInputStream());
            streamOut.writeUTF(username);
        }catch(Exception e){
            System.out.println("Something went wrong connecting to that host");
        }
        //their username is then sent to the server
        try{
            handOff=Integer.parseInt(streamIn.readUTF());
            streamOut.close();
            streamIn.close();
            socket.close();
            socket=new Socket(IP,handOff);
            streamOut=new DataOutputStream(socket.getOutputStream());
            streamIn=new DataInputStream(socket.getInputStream());
        }catch(Exception e){
            System.out.println("Something went wrong handing off to another port");
        }
        /* The server talks to each client on a different port. When a client connects,
         * it joins a default port before being given a handoff port. The client recieves 
         * the handoff port from the server, closes its connections and reconnects on the new port
         */
        System.out.println("Joined "+IP+" successfully!");
        try{
            System.out.println("Waiting for game to start");
            incoming=streamIn.readUTF();
            System.out.println(incoming);
            if(incoming.equals("Game has started!")) playing=true;
            //once two players have connected to the server it will send the message saying the game has started
            while(playing){
                System.out.println("Would you like to snitch on your partner?");
                while(!actionChosen){
                    switch(input.nextLine()){
                        case("y"):
                        case("yes"):
                        case("snitch"):
                                        System.out.println("You have chosen to snitch");
                                        action="SNITCH";
                                        actionChosen=true;
                                        break;
                        case("n"):
                        case("no"):
                        case("silent"):
                        case("stay silent"):
                                            System.out.println("You have chosen to stay silent");
                                            action="SILENT";
                                            actionChosen=true;
                                            break;
                        default:
                                System.out.println("Please choose to snitch or stay silent");
                                break;
                    }
                }
                //the player chooses what action to take. invalid choices are not accepted
                streamOut.writeUTF(action);
                actionChosen=false;
                incoming=streamIn.readUTF();
                /* the action is sent and then the client reads in a message from the server.
                 * if this is a normal round the message will contain what the other player chose.
                 * however, if this is the last round, the message will instead be "Game over!".
                 */
                if(incoming.equals("SNITCH") || incoming.equals("SILENT")){ 
                    System.out.println("Your opponent chose: "+incoming);
                    score=Double.parseDouble(streamIn.readUTF());
                    oppScore=Double.parseDouble(streamIn.readUTF());
                    System.out.println("Score: "+(int)score+"\n"
                                       +"Opponent score: "+(int)oppScore);
                    /* if the message contains the opponents move, it is printed out.
                     * the server then waits for two more messages containing the current 
                     * score of both players.
                     */
                }else if (incoming.equals("Game over!")){ 
                    System.out.println(incoming);
                    playing=false;
                    //otherwise if the message is game over the game will stop
                }else{
                    System.out.println("Unexpected message received");
                }    
                streamOut.flush();
            }
            try{
                score=Double.parseDouble(streamIn.readUTF());
                oppScore=Double.parseDouble(streamIn.readUTF());
                roundsPlayed=Double.parseDouble(streamIn.readUTF());
                System.out.println("Your final score: "+(int)score);
                System.out.println("Your opponents final score: "+(int)oppScore);
                System.out.println("You played: "+(int)roundsPlayed+" rounds");
            }catch(Exception e1){
                System.out.println("There was an error receiving scores");
            }
            /* after the game is finished the server will send two final messages,
             * which are the players final score and the number of rounds played
             * The client prints out both players score to let them know how they did.
             */
            streamOut.close();
        }catch(Exception e){
            System.out.println("Something went wrong playing the game");
        }
        saveScores();
        //finally, the scores are saved
    }
    public void accessScores(){
        try{
            Scanner fileReader=new Scanner(playerScore);
            scoreLine=fileReader.nextLine().split(",");
            totalScore=Double.parseDouble(scoreLine[1]);
            roundsPlayedLine=fileReader.nextLine().split(",");
            totalRoundsPlayed=Double.parseDouble(roundsPlayedLine[1]);
            if(totalScore!=0)gameplayRating=totalRoundsPlayed/totalScore;
            usernameLine=fileReader.nextLine().split(",");
            username=usernameLine[1];
            /*This reads the file to find the players overall score and rounds played
             * including ones from previous sessions. It does this by seperating the csv
             * file using a split function to find commmas.
             */
        }catch(IOException e){
            System.out.println("There was an error retrieving your past scores");
        }
    }
    public void saveScores(){
        try{
            System.out.println("Saving scores");
            FileWriter scoreTracker=new FileWriter(playerScore);
            scoreTracker.append("score");
            scoreTracker.append(",");
            scoreTracker.append(String.valueOf(score+totalScore));
            scoreTracker.append("\n");
            scoreTracker.append("roundsplayed");
            scoreTracker.append(",");
            scoreTracker.append(String.valueOf(roundsPlayed+totalRoundsPlayed));
            scoreTracker.append("\n");
            scoreTracker.append("username");
            scoreTracker.append(",");
            scoreTracker.append(username);
            scoreTracker.flush();
            scoreTracker.close();
            //Updates the total score, rounds played and username across mutiple sessions
        }catch(IOException e){
            System.out.println("There was an error saving your scores.");
        }
    }
}
