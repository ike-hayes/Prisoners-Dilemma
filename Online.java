/**
 * Write a description of class Online here.
 *
 * @author (your name)
 * @version (a version number or a date)
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
    double oppScore;
    double totalScore;
    double roundsPlayed;
    double totalRoundsPlayed;
    double gameplayRating;
    String scoreLine[];
    String roundsPlayedLine[];
    String username;
    String usernameLine[];
    boolean usernameChosen=false;
    boolean hostChosen=false;
    boolean host;
    /**
     * Constructor for objects of class Online
     */
    public Online()
    {
        // initialise instance variables
        accessScores();
        chooseUsername();
        //Here the player selects their username, unless they already have one
        System.out.println("You have played: "+(int)totalRoundsPlayed+" rounds \n"
                            +"You are serving: "+(int)totalScore+" years in prison");
        if(totalScore==0){
            System.out.println("Your gameplay rating is: Perfect! \n");
        }else{
            System.out.println("Your gameplay rating is: "+gameplayRating+"\n");
        }
        System.out.println("Would you like to host a game or join an existing one?");
        while(!hostChosen){
            switch(input.nextLine()){
                case("host"):host=true; 
                             break;
                case("join"):host=false;
                             break;
                default:System.out.println("Please choose host or join");
                        break;
            }
        }
        if(host){
            new ServerSide();
        }else{
            System.out.println("Enter the IP address you would like to connect to");
            new ClientSide(input.nextLine());
        }
        
        saveScores();
    }
    public void chooseUsername(){
        if(usernameLine.length<2){
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
        }else{
            username=usernameLine[1];
            System.out.println("Welcome back, "+username);
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
