/**
 * Write a description of class Online here.
 *
 * @author Ike Hayes
 * @version 14/6/21
 */
import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.*;
import java.net.*;
import java.net.InetAddress;
//importing the extra features my program uses - it will need to read and write files.
public class Online
{
    File playerScore=new File("onlinescore.csv"); 
    Scanner input=new Scanner(System.in);
    //here I create the file used to read and write the players score, as well as the scanner to take input
    double score;
    double totalScore;
    double roundsPlayed;
    double totalRoundsPlayed;
    double gameplayRating;
    String scoreLine[];
    String roundsPlayedLine[];
    String username;
    String usernameLine[];
    String hostIP;
    boolean usernameChosen=false;
    boolean hostChosen=false;
    boolean host;
    boolean clientConnected=false;
    /**
     * Constructor for objects of class Online
     */
    public Online()
    {
        // initialise instance variables
        accessScores();
        if(usernameLine.length<2){
            chooseUsername();
        }else{
            username=usernameLine[1];
            System.out.println("Welcome back, "+username);
        }
        //Here the player selects their username, unless they already have one
        System.out.println("You have played: "+(int)totalRoundsPlayed+" rounds \n"
                            +"You are serving: "+(int)totalScore+" years in prison");
        if(totalScore==0){
            System.out.println("Your gameplay rating is: Perfect! \n");
        }else{
            System.out.println("Your gameplay rating is: "+gameplayRating+"\n");
        }
        saveScores();
        System.out.println("Would you like to host a game or join an existing one?");
        while(!hostChosen){
            switch(input.nextLine()){
                case("host"):host=true; 
                             hostChosen=true; 
                             break;
                case("join"):host=false;
                             hostChosen=true;
                             break;
                default:System.out.println("Please choose host or join");
                        break;
            }
        }
        if(host){
            try{
                ServerSide serverHost=new ServerSide();
                ClientSide player=new ClientSide("localhost");
            }catch(Exception e){
                System.out.println("Something went wrong hosting the server");
            }
        }else{
            while(!clientConnected){
                System.out.println("Enter the IP address you would like to connect to");
                hostIP=input.nextLine();
                try{
                    ClientSide player=new ClientSide(hostIP);
                    clientConnected=true;
                }catch(Exception e){
                    System.out.println("Something went wrong connecting to that host");
                    clientConnected=false;
                }
            }
        }
    }
    public void chooseUsername(){
        System.out.println("Please choose a username. This can be changed at any time");
        while(!usernameChosen){
            username=input.nextLine();
            System.out.println("You are "+username+"? y/n");
            switch(input.nextLine()){
                case("y"):
                case("yes"):
                            usernameChosen=true;
                            break;
                case("n"):
                case("no"):
                           System.out.println("Enter your username again");
                           break;
                default:
                       System.out.println("Enter your username again");
                       break;   
            }
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
