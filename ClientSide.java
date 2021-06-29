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
    File playerScore=new File("onlinescore.csv"); 
    Scanner input=new Scanner(System.in);
    String scoreLine[];
    String roundsPlayedLine[];
    String username;
    String usernameLine[];
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
    /**
     * Constructor for objects of class ClientSide
     */
    public ClientSide(String IP)
    {
        // initialise instance variables
        accessScores();
        try{
            socket=new Socket(IP,PORT);
            streamOut=new DataOutputStream(socket.getOutputStream());
            streamIn=new DataInputStream(socket.getInputStream());
            streamOut.writeUTF(username);
        }catch(Exception e){
            System.out.println("Something went wrong connecting to that host");
        }
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
        System.out.println("Joined "+IP+" successfully!");
        try{
            System.out.println("Waiting for game to start");
            incoming=streamIn.readUTF();
            System.out.println(incoming);
            if(incoming.equals("Game has started!")) playing=true;
            while(playing){
                System.out.println("Would you like to snitch on your partner?");
                while(!actionChosen){
                    switch(input.nextLine()){
                        case("y"):
                        case("yes"):
                        case("snitch"):
                                        action="SNITCH";
                                        actionChosen=true;
                                        break;
                        case("n"):
                        case("no"):
                        case("silent"):
                        case("stay silent"):
                                            action="SILENT";
                                            actionChosen=true;
                                            break;
                        default:
                                System.out.println("Please choose to snitch or stay silent");
                                break;
                    }
                }
                streamOut.writeUTF(action);
                actionChosen=false;
                incoming=streamIn.readUTF();
                if(incoming.equals("SNITCH") || incoming.equals("SILENT")){ 
                    System.out.println("Your opponent chose: "+incoming);
                } 
                if(incoming.equals("Game over!")){ 
                    System.out.println(incoming);
                    playing=false;
                }else{
                    System.out.println("Score: "+streamIn.readUTF()+"\n"
                                       +"Opponent score: "+streamIn.readUTF());
                }
                streamOut.flush();
            }
            streamOut.close();
        }catch(Exception e){
            System.out.println("Something went wrong playing the game");
        }
        try{
            score=Double.parseDouble(streamIn.readUTF());
            saveScores();
        }catch(Exception e){
            System.out.println("There was an error saving your scores.");
        }
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
            //This code is inside a try statement so I can print something out if something goes wrong
        }
    }
    public void saveScores(){
        try{
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
            //Updates the total score and rounds played across mutiple sessions
        }catch(IOException e){
            System.out.println("There was an error saving your scores.");
        }
    }
}
