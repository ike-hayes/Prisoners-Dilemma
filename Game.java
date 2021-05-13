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
    String action;
    String oppAction;
    boolean playing=true;
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
                            +"Each round, you and your partner will have a choice \n"
                            +"You can snitch on your partner, or stay silent \n"
                            +"Here's what happens after that \n"
                            +"\n"
                            +"You both stay silent: 2 years added to both your sentences \n"
                            +"You snitch, they don't: No time added to your sentence, but 5 to theirs \n"
                            +"They snitch, you don't: 5 years added to your sentence, none to theirs \n"
                            +"You both snitch: 4 years added to both your sentences \n"
                            +"Try to keep your sentence as low as possible, lower than your partner! \n"
                            +"\n"
                            +"Let's play!");
        while(playing){
            System.out.println("Would you like to snitch on your partner? Yes/No \n"
                                +"Type 'stop' to stop playing");
            action=input.nextLine().toLowerCase();
            switch(action){
                case("y"):
                case("yes"):
                            if(oppAction.equals("snitch")){
                                System.out.println("You and your partner snitched on each other!");
                                oppScore+=4;
                                score+=4;
                            }else{
                                System.out.println("You snitched, and your partner stayed silent!");
                                oppScore+=5;
                            }
                            break;
                case("n"):
                case("no"):
                            if(oppAction.equals("snitch")){
                                System.out.println("Your partner snitched on you, and you stayed silent!");
                                score+=5;
                            }else{
                                System.out.println("You and your partner stayed silent!");
                                score+=2;
                                oppScore+=2;
                            }
                            break;
                case("stop"):
                             playing=false;
                             break;
                default:System.out.println("Choose yes to snitch, or no to stay silent");
                        break;
            }
            System.out.println("You are serving "+score+" years in prison \n"
                               +"Your partner is serving "+oppScore+" years in prison");
        }
        System.out.println("Thank you for playing! Here are your final scores: \n"
                            +"You will serve "+score+" years in prison \n"
                            +"Your partner will serve "+oppScore+" years in prison");
        if(oppScore>score){
            System.out.println("Looks like you came out better off!");
        }else if(score>oppScore){
            System.out.println("Looks like your partner came out better off!");
        }else{
            System.out.println("You and your partner will serve the same time");
        }
    }
    public void alwaysSilentAI(){
        oppAction="silent";
    }
    public void alwaysSnitchAI(){
        oppAction="snitch";
    }
    public void titForTatAI(){
        oppAction=action;
    }
}
