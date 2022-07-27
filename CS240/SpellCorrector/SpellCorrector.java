package spell;

import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class SpellCorrector implements ISpellCorrector{

    static private int count;

    public Trie dict;

    public SpellCorrector(){
        dict = new Trie();
        count = 0;
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {

        File file = new File(dictionaryFileName);
        Scanner scan = new Scanner(file);

        while(scan.hasNext()){
            String s = scan.next();
            s = s.toLowerCase();
            dict.add(s);
            //System.out.println("Adding next word to the dictionary: " + s);
        }

    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        String suggestion = null;
        count = 0;

        inputWord = inputWord.toLowerCase();

        System.out.println("inputWord = " + inputWord);

        if (inputWord == ""){
            return suggestion;
        }

        if (dict.find(inputWord) != null){
            return inputWord;
        }


        suggestion = getOneDistanceSuggestion(inputWord, suggestion);


        //System.out.println("Passed first part");
        if (dict.find(suggestion) == null){
            suggestion = null;
        }

        if (suggestion == null && count == 0) {


            //suggestion = getTwoDistanceSuggestion(inputWord);

            ArrayList<String> deletion = new ArrayList<String>();
            deletion = makeDeletionList(inputWord);

            ArrayList<String> transposition = new ArrayList<String>();
            transposition = makeTranspositionList(inputWord);

            ArrayList<String> alteration = new ArrayList<String>();
            alteration = makeAlterationList(inputWord);

            ArrayList<String> insertion = new ArrayList<String>();
            insertion = makeInsertionList(inputWord);

            System.out.println("Passed second part");
            System.out.println("deletion size: " + deletion.size());
            for (int i = 0; i < deletion.size(); i++){
                suggestion = getOneDistanceSuggestion(deletion.get(i), suggestion);
            }

            System.out.println("transposition size: " + transposition.size());
            for (int i = 0; i < transposition.size(); i++){
                suggestion = getOneDistanceSuggestion(transposition.get(i), suggestion);
            }

            System.out.println("alteration size: " + alteration.size());
            for (int i = 0; i < alteration.size(); i++){
                System.out.println("Current word: " + alteration.get(i));
                suggestion = getOneDistanceSuggestion(alteration.get(i), suggestion);
            }

            System.out.println("insertion size: " + insertion.size());
            for (int i = 0; i < insertion.size(); i++){
                suggestion = getOneDistanceSuggestion(insertion.get(i), suggestion);
            }


        }

        return suggestion;
    }

    /*
    public String getTwoDistanceSuggestion(String input){
        String suggestion = null;

        ArrayList<String> deletion = new ArrayList<String>();
        deletion = makeDeletionList(inputWord);

        ArrayList<String> transposition = new ArrayList<String>();
        transposition = makeTranspositionList(inputWord);

        ArrayList<String> alteration = new ArrayList<String>();
        alteration = makeAlterationList(inputWord);

        ArrayList<String> insertion = new ArrayList<String>();
        insertion = makeInsertionList(inputWord);


        ArrayList<ArrayList<String>> deletion_deletion = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < deletion.size(); i++){
            ArrayList<String> s = new ArrayList<String>();
            s.add(getOneDistanceSuggestion());
        }


        return suggestion;
    }
     */

    public String getOneDistanceSuggestion(String inputWord, String currentSuggestion){


        ArrayList<String> deletion = new ArrayList<String>();
        deletion = makeDeletionList(inputWord);

        ArrayList<String> transposition = new ArrayList<String>();
        transposition = makeTranspositionList(inputWord);

        ArrayList<String> alteration = new ArrayList<String>();
        alteration = makeAlterationList(inputWord);

        ArrayList<String> insertion = new ArrayList<String>();
        insertion = makeInsertionList(inputWord);

        /*
        String a = "aa";
        String b = "bb";

        if (a.compareTo(b) < 0){
            System.out.println(a + " compared to " + b + " results in a number less than 0");
        }
         */
        System.out.println("Gate1");
        for (int i = 0; i < deletion.size(); i++){
            INode tn = new TrieNode();
            //System.out.println(deletion.get(i) + " in deletion.");
            tn = dict.find(deletion.get(i));
            if (tn != null){
                if (tn.getValue() > count || currentSuggestion == null || (tn.getValue() == count && deletion.get(i).compareTo(currentSuggestion) < 0)){
                    //System.out.println("Setting suggestion to: " + deletion.get(i));
                    currentSuggestion = deletion.get(i);
                    count = tn.getValue();
                }
            }
        }
        System.out.println("Gate2");
        for (int i = 0; i < transposition.size(); i++){
            INode tn = new TrieNode();
            //System.out.println(transposition.get(i) + " in transposition.");
            tn = dict.find(transposition.get(i));
            if (tn != null){
                if (tn.getValue() > count || currentSuggestion == null || (tn.getValue() == count && transposition.get(i).compareTo(currentSuggestion) < 0)){
                    currentSuggestion = transposition.get(i);
                    count = tn.getValue();
                }
            }
        }
        System.out.println("Gate3");
        for (int i = 0; i < alteration.size(); i++){
            INode tn = new TrieNode();
            //System.out.println(alteration.get(i) + " in alteration.");
            tn = dict.find(alteration.get(i));
            if (tn != null){
                if (tn.getValue() > count || currentSuggestion == null || (tn.getValue() == count && alteration.get(i).compareTo(currentSuggestion) < 0)){
                    currentSuggestion = alteration.get(i);
                    count = tn.getValue();
                }
            }
        }
        System.out.println("Gate4");
        for (int i = 0; i < insertion.size(); i++){
            INode tn = new TrieNode();
            //System.out.println(insertion.get(i) + " in insertion.");
            tn = dict.find(insertion.get(i));
            if (tn != null){
                if (tn.getValue() > count || currentSuggestion == null || (tn.getValue() == count && insertion.get(i).compareTo(currentSuggestion) < 0)){
                    currentSuggestion = insertion.get(i);
                    count = tn.getValue();
                }
            }
        }

        System.out.println("Returning " + currentSuggestion);
        return currentSuggestion;
    }


    public static ArrayList<String> makeDeletionList(String input){
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < input.length(); i++){
            StringBuilder s = new StringBuilder(input);
            s.deleteCharAt(i);
            list.add(s.toString());
        }
        return list;
    }

    public static ArrayList<String> makeTranspositionList(String input){
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0 ; i < input.length() - 1; i++){
            StringBuilder s = new StringBuilder(input);
            StringBuilder helper = new StringBuilder("");
            helper.append(input.charAt(i + 1));
            helper.append(input.charAt(i));
            //System.out.println("Helper = " + helper.toString());
            s.replace(i, i + 2, helper.toString());
            list.add(s.toString());
        }
        return list;
    }

    public static ArrayList<String> makeAlterationList(String input){
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < input.length(); i++){
            for (int j = 0; j < 26; j++){
                StringBuilder s = new StringBuilder(input);
                char c = (char)('a' + j);
                StringBuilder helper = new StringBuilder("");
                helper.append(c);
                s.replace(i, i + 1, helper.toString());
                list.add(s.toString());
            }

        }
        return list;
    }

    public static ArrayList<String> makeInsertionList(String input){
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < input.length() + 1; i++){
            for (int j = 0; j < 26; j++){
                StringBuilder s = new StringBuilder(input);
                char c = (char)('a' + j);
                StringBuilder helper = new StringBuilder("");
                helper.append(c);
                s.insert(i, c);
                list.add(s.toString());
            }
        }
        return list;
    }


}
