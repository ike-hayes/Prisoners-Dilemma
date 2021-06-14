/**
* Write a description of class Game here.
*
* @author Ike Hayes
* @version 11/6/21
*/
import java.util.Scanner;
public class Game
{
    Scanner input=new Scanner(System.in);
    //here I create the scanner to take input
    boolean online;
    boolean gameTypeChosen=false;
    //creating the variables used in the set up for the game
    /**
    * Constructor for objects of class Game
    */
    public Game()
    {
        System.out.println("Welcome to the Prisoner's Dilemma! \n"
                    +"How to play:\n"
                    +"\n"
                    +"You and your partner in crime have been caught! \n"
                    +"The police are seeking definitive information to put you away \n"
                    +"Each round, you and your partner will have a choice \n"
                    +"You can snitch on your partner, or stay silent \n"
                    +"Here's what happens after that \n"
                    +"\n"
                    +"You both stay silent: 2 years added to both your sentences \n"
                    +"You snitch, they don't: No time added to your sentence, but 5 to theirs \n"
                    +"They snitch, you don't: 5 years added to your sentence, none to theirs \n"
                    +"You both snitch: 4 years added to both your sentences \n"
                    +"Try to keep your sentence as low as possible! \n"
                    +"\n"
                    +"Let's play!");
        //Explaing how to play to the player
        System.out.println("Would you like to play offline, or online? \n" 
                           +"Your offline and online scores are saved seperatly");
        while(!gameTypeChosen){
            if(input.nextLine().equals("offline")){
                online=false;  
                gameTypeChosen=true;
            }else if(input.nextLine().equals("online")){
                online=true;   
                gameTypeChosen=true;
            }else {
                System.out.println("Please choose offline or online");
            }
        }
        //here the player choose whether they would like to play online or offline
        if(online){
            new Online();
        }else{
            new SinglePlayer();
        }
    }
}