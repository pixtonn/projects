package hangman;

//import sun.invoke.empty.Empty;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.SortedSet;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.HashMap;

public class EvilHangmanGame implements IEvilHangmanGame {

    static int NUM_GUESSES = 6;
    Set<String> dict = new TreeSet<String>();
    SortedSet<Character> guessedLetters = new TreeSet<Character>();
    private EmptyDictionaryException EmptyDictionaryException = new EmptyDictionaryException();
    private IOException ex = new IOException();
    public StringBuilder currentWord = new StringBuilder("");
    public int guessesLeft = NUM_GUESSES;
    private GuessAlreadyMadeException GuessAlreadyMadeException = new GuessAlreadyMadeException();
    public boolean gameOver = false;

    public EvilHangmanGame(){

    }

    public void outputDict(){
        Iterator<String> itr = dict.iterator();
        while(itr.hasNext()){ System.out.println(itr.next()); }

    }

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {

        dict = new TreeSet<String>();
        guessedLetters = new TreeSet<Character>();
        currentWord = new StringBuilder("");

        for (int i = 0; i < wordLength; i++){
            currentWord.append("-");
        }

        Scanner in = new Scanner(dictionary);

        if(dictionary.length() == 0){
            throw EmptyDictionaryException;
        }

        try {
            while (in.hasNext()) {
                String toAdd = in.next();
                if (toAdd.length() == wordLength) {
                    dict.add(toAdd);
                }
            }
            if(false){
                throw ex;
            }

        }
        catch(IOException ex) {
            System.out.println("IO Error");
        }
        if (dict.size() == 0){
            throw EmptyDictionaryException;
        }

        //outputDict();
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {

        guess = Character.toLowerCase(guess);
        if (guessedLetters.contains(guess)){
            throw GuessAlreadyMadeException;
        }
        guessedLetters.add(guess);

        HashMap<String, TreeSet<String>> patterns = new HashMap<String, TreeSet<String>>();
        patterns = makePatterns(guess);
        String pattern = decidePattern(patterns, guess);
        //System.out.println("Pattern chosen: " + pattern);
        addToCurr(pattern);
        //System.out.println("Newly updated word: " + currentWord.toString());
        dict = patterns.get(pattern);
        return patterns.get(pattern);
    }

    public void addToCurr(String pattern){
        for (int i = 0; i < pattern.length(); i++){
            if (pattern.charAt(i) != '-'){
                currentWord.setCharAt(i, pattern.charAt(i));
            }
        }
    }

    public String decidePattern(HashMap<String, TreeSet<String>> patterns, char guess){
        //check which set contains the most
        String maxPattern = "";
        int maxNum = 0;
        for (String pattern : patterns.keySet()){
            //System.out.println("Checking the pattern: " + pattern);
            //System.out.println("Words that follow the pattern: " + patterns.get(pattern).size());
            if (patterns.get(pattern).size() > maxNum){
                maxNum = patterns.get(pattern).size();
                maxPattern = pattern;
                //System.out.println("maxPattern set to: " + pattern);
            }
            else if (patterns.get(pattern).size() == maxNum){
                int first = 0; // number of the guessed letter in the pattern
                int second = 0;
                for (int i = 0; i < maxPattern.length(); i++){
                    if (maxPattern.charAt(i) == guess){
                        first++;
                    }
                    if (pattern.charAt(i) == guess){
                        second++;
                    }
                    //System.out.println("Done checking the " + i + "th position in the patterns");
                }
                if (first == second){//now to find the one with the rightmost guessed letter
                    for (int i = maxPattern.length() - 1; i > -1; i--){
                        //System.out.println("Comparing the positions at " + i);
                        if (pattern.charAt(i) == guess && maxPattern.charAt(i) != guess){
                            maxPattern = pattern;
                            //System.out.println("1maxPattern set to: " + pattern);
                        }
                        else if (pattern.charAt(i) == guess && maxPattern.charAt(i) == guess){
                            for (int j = i; j >= 0; j--){
                                if (pattern.charAt(j) == guess && maxPattern.charAt(j) != guess){
                                    //System.out.println("maxPattern[" + j + "] = " + maxPattern.charAt(j) +" while pattern[" + j + "] = " + pattern.charAt(j));
                                    maxPattern = pattern;
                                    //System.out.println("2maxPattern set to: " + pattern);
                                }
                                else if (pattern.charAt(j) != guess && maxPattern.charAt(j) == guess){
                                    j = -1;
                                }
                            }

                        }
                        else if (pattern.charAt(i) != guess && maxPattern.charAt(i) == guess){
                            i = -1;
                        }
                    }
                }
                else if (first > second){
                    maxPattern = pattern;
                    //System.out.println("maxPattern set to: " + pattern);
                }
            }
        }
        return maxPattern;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public HashMap<String, TreeSet<String>> makePatterns(char guess){
        HashMap<String, TreeSet<String>> patterns = new HashMap<String, TreeSet<String>>();
        Iterator<String> itr = dict.iterator();
        while(itr.hasNext()){
            String word = itr.next();
            StringBuilder stb = new StringBuilder(word);
            for (int i = 0; i < stb.length(); i++){
                if (stb.charAt(i) != guess) {
                    stb.setCharAt(i, '-');
                }
            }
            if (patterns.containsKey(stb.toString())){
                TreeSet<String> set = new TreeSet<String>();
                set = patterns.get(stb.toString());
                set.add(word);
                patterns.put(stb.toString(), set);
            }
            else{
                TreeSet<String> set = new TreeSet<String>();
                set.add(word);
                patterns.put(stb.toString(), set);
            }
        }
        return patterns;
    }

    public static void main(String args[]){

    }
}
