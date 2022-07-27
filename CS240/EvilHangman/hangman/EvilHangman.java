package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class EvilHangman {

    public static void main(String[] args) {
        File file = new File(args[0]);
        EvilHangmanGame game = new EvilHangmanGame();
        int wordLength = Integer.parseInt(args[1]);
        game.guessesLeft = Integer.parseInt(args[2]);

        try {
            game.startGame(file, wordLength);
        }
        catch(IOException ex){
            System.out.println("IO Error");
        }
        catch(EmptyDictionaryException ex){
            System.out.println("Empty Dictionary");
        }


        while(!game.gameOver){
            char c;
            Scanner scan = new Scanner(System.in);
            System.out.println("Guesses left: " + game.guessesLeft);
            System.out.println("Already guessed: " + game.guessedLetters.toString());
            System.out.println(game.currentWord.toString());
            System.out.println("Please make a guess: ");
            c = scan.next().charAt(0);
            while (!Character.isLetter(c)){
                System.out.println("That's not a letter. Please guess a valid letter.");
                c = scan.next().charAt(0);
            }
            boolean correctGuess = false;
            StringBuilder curr = new StringBuilder(game.currentWord.toString());
            while(!correctGuess) {
                try {
                    game.makeGuess(c);
                    correctGuess = true;
                } catch (GuessAlreadyMadeException ex) {
                    System.out.println("You already used that letter. Please guess a letter you have not previously guessed.");
                    c = scan.next().charAt(0);
            while (!Character.isLetter(c)){
                System.out.println("That's not a letter. Please guess a valid letter.");
                c = scan.next().charAt(0);
            }
                }
            }
            //System.out.println("Comparing " + game.currentWord.toString() + " and " + curr.toString());
            if (curr.toString().equals(game.currentWord.toString())){
                game.guessesLeft = game.guessesLeft - 1;
            }
            if (game.guessesLeft == 0){
                Iterator<String> itr = game.dict.iterator();
                System.out.println("You lost. The word was: " + itr.next());
                game.gameOver = true;
            }
            if (game.currentWord.indexOf("-") == -1){
                Iterator<String> itr = game.dict.iterator();
                System.out.println("Congratulations!. You win. The word was " + itr.next());
                game.gameOver = true;
            }
        }
        return;
    }

}
