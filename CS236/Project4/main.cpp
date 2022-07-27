
#include "Token.h"
#include "DatalogProgram.h"
#include "Predicate.h"
#include "Parser.h"
#include "Database.h"
#include <vector>
#include <iostream>

int main(int argc, const char * argv[]) {
    ifstream in(argv[1]);
    if (!in.is_open()) {
        cout << argv[1] << " didn't open as the input stream" << endl;
        return 1;
    }
    
    vector<Token> list;
    int lineNum = 1;
    char c;
    while(in.get(c)){
        
    L1://I use goto to come here if I don't want to read in the next character
        if (isalpha(c)){
            string ID = "";
            ID += c;
            bool digitPassed = false;
            while(in.get(c)){
                if(isalpha(c) && digitPassed == false){
                    ID += c;
                }
                else if(isdigit(c)){
                    ID += c;
                    digitPassed = true;
                }
                else if(isspace(c)){
                    if (ID == "Queries"){
                        Token t("QUERIES", "Queries", lineNum);
                        list.push_back(t);
                        break;
                    }
                    else if(ID == "Schemes"){
                        Token t("SCHEMES", "Schemes", lineNum);
                        list.push_back(t);
                        break;
                    }
                    else if(ID == "Facts"){
                        Token t("FACTS", "Facts", lineNum);
                        list.push_back(t);
                        break;
                    }
                    else if(ID == "Rules"){
                        Token t("RULES", "Rules", lineNum);
                        list.push_back(t);
                        break;
                    }
                    else{
                        Token t("ID", ID, lineNum);
                        list.push_back(t);
                        break;
                    }
                }
                else{
                    if (ID == "Queries"){
                        Token t("QUERIES", "Queries", lineNum);
                        list.push_back(t);
                        break;
                    }
                    else if(ID == "Schemes"){
                        Token t("SCHEMES", "Schemes", lineNum);
                        list.push_back(t);
                        break;
                    }
                    else if(ID == "Facts"){
                        Token t("FACTS", "Facts", lineNum);
                        list.push_back(t);
                        break;
                    }
                    else if(ID == "Rules"){
                        Token t("RULES", "Rules", lineNum);
                        list.push_back(t);
                        break;
                    }
                    else{
                        Token t("ID", ID, lineNum);
                        list.push_back(t);
                        goto L1; //here I need to use the next character because it is not whitespace, but it is
                    }
                }
            }
        }
        
        
        switch(c){
            case('\n'):{
                lineNum++;
                break;
            }
            case(' '):{
                break;
            }
            case('\t'):{
                break;
            }
            case(','):{
                Token t("COMMA", ",", lineNum);
                list.push_back(t);
                break;
            }
            case('.'):{
                Token t("PERIOD", ".", lineNum);
                list.push_back(t);
                break;
            }
            case('?'):{
                Token t("Q_MARK", "?", lineNum);
                list.push_back(t);
                break;
            }
            case('('):{
                Token t("LEFT_PAREN", "(", lineNum);
                list.push_back(t);
                break;
            }
            case(')'):{
                Token t("RIGHT_PAREN", ")", lineNum);
                list.push_back(t);
                break;
            }
            case(':'):{
                if (in.peek() == '-'){
                    in.get();
                    Token t("COLON_DASH", ":-", lineNum);
                    list.push_back(t);
                }
                else{
                    Token t("COLON", ":", lineNum);
                    list.push_back(t);
                }
                break;
            }
            case('*'):{
                Token t("MULTIPLY", "*", lineNum);
                list.push_back(t);
                break;
            }
            case('+'):{
                Token t("ADD", "+", lineNum);
                list.push_back(t);
                break;
            }
                ////////////string case
            case('\''):{
                string fullString = "";
                fullString += c;
                int extraLines = 0; //lines to add to counter after string, that were contained in the string
                bool tokenCreated = false;
                while(in.get(c)){
                    if (c == '\n'){
                        extraLines++;
                    }
                    if(c == '\''){ //if an ending quotation mark is found...
                        if (in.peek() == '\''){ //if there is a second one, add them to the string
                            in.get(); //take the second quotation mark off
                            fullString += "\'\'"; //add them to the string
                        }
                        else{ //else if it isn't a double quotation mark, end the string
                            fullString += c;
                            Token t("STRING", fullString, lineNum);
                            tokenCreated = true;
                            list.push_back(t);
                            lineNum += extraLines;
                            break;
                        }
                    }
                    else{ //else if it isn't an ending quotation mark, just add it to the string
                        fullString += c;
                    }
                }
                if (tokenCreated == false){ // only occurs if EOF is reached before ending quotation mark
                    Token t("UNDEFINED", fullString, lineNum);
                    list.push_back(t);
                    lineNum += extraLines;
                }
                break;
            }
                
                
                
                /////////////////comment case
            case('#'):{
                int extraLines = 0;
                bool tokenCreated = false;
                string fullComment = "";
                if (in.peek() == '|'){ //if it has a | need to watch for ending |#
                    in.get();
                    fullComment = "#|";
                    while(in.peek() && in.get(c)){ // the peek happens and if it is EOF, the loop will get cut
                        if(c == '\n'){ //add to lines after creation of the token
                            extraLines++;
                        }
                        if (c == '|' && in.peek() == '#'){ // if the end of the block comment is reached
                            in.get(); //pop off the #
                            fullComment += "|#";
                            Token t("COMMENT", fullComment, lineNum);
                            //list.push_back(t);
                            lineNum += extraLines;
                            tokenCreated = true;
                            break;
                        }
                        // if the above if statement doesn't happen and break out of the loop, this executes
                        fullComment += c;
                    }
                }
                else{
                    fullComment = "#";
                    while(in.get(c)){
                        if (c == '\n'){ // reaches end of line in single line comment
                            Token t("COMMENT", fullComment, lineNum);
                            lineNum++;
                            //list.push_back(t);
                            tokenCreated = true;
                            break;
                        }
                        fullComment += c;
                    }
                }
                if (tokenCreated == true){ // the break above only takes it out of the loop, and I need this to get out of the switch
                    break;
                }
                if (tokenCreated == false){ // only occurs if EOF is reached before ending comment
                    Token t("UNDEFINED", fullComment, lineNum);
                    lineNum += extraLines;
                    //list.push_back(t);
                    break;
                }
            }
                
                
                
                
                
            default:{
                if(isalpha(c)){
                    // do nothing, it was already done at the top in the if statements; because it wasn't done in the switch I check here
                }
                else{ // if this is entered, it must be undefined
                    string myString = "";
                    myString += c;
                    Token t("UNDEFINED", myString, lineNum);
                    list.push_back(t);
                    break;
                }
            }
        }
    }
    Token t("EOF", "", lineNum);
    list.push_back(t);
    
    
    //Output all tokens
    /*
    for (size_t i = 0; i < list.size(); i++){
        cout << list.at(i) << endl;
    }
    
    cout << "Total Tokens = " << list.size() << endl;
    */
    
    
    //BEGIN PROJECT 2
    
    Parser myParse(list);
    myParse.Parse();
    //cout << myParse.prog.getSchemes().size();
    
    
    
    //BEGIN PROJECT 4
    cout << "Rule Evaluation" << endl;
    Database DB(myParse.getDP());
    size_t totalPasses = DB.solveRules();
    cout << endl << "Schemes populated after " << totalPasses << " passes through the Rules." << endl << endl;
    //cout << "Total tuples: " << DB.countTuples() << endl;
    
    //BEGIN PROJECT 3
    
    cout << "Query Evaluation" << endl;
    DB.solveQueries();
    
    //cout << DB.toString();
    
    
    
    
    return 0;
}

