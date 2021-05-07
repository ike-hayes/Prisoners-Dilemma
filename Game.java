/**
 * Write a description of class Game here.
 *
 * @author Ike Hayes
 * @version (a version number or a date)
 */
import java.util.Scanner;
public class Game
{
    // instance variables - replace the example below with your own
    Scanner input=new Scanner(System.in);
    int score;
    int oppScore;
    /**
     * Constructor for objects of class Game
     */
    public Game()
    {
        // initialise instance variables
        System.out.println("Welcome to the Prisoner's Dilemma! \n"
                            +"How to play:\n"
                            +"\n"
                            +"You and your partner in crime have been caught! \n"
                            +"The police are seeking definitive information to put you away \n"
                            +"Each round, you and your opponent will have a choice \n"
                            +"You can snitch on your oppponent, or stay silent \n"
                            +"Here's what happens after that \n"
                            +"\n"
                            +"You both stay silent: 2 years added to both your sentences \n"
                            +"You snitch, they don't: No time added to your sentence, but 5 to theirs \n"
                            +"They snitch, you don't: 5 years added to your sentence, none to theirs \n"
                            +"You both snitch: 10 years added to both your sentences \n"
                            +"\n"
                            +"Let's play!");
        while(true){
            
        }
    }
}
