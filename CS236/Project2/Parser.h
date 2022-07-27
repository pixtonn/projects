

#ifndef Parser_h
#define Parser_h

#include "DatalogProgram.h"
#include <vector>
#include <iostream>
#include <set>


using namespace std;

class Parser{
private:
    size_t index;
    DatalogProgram prog;
    Predicate b;
    set<string> dom;
    Rule r;
    bool isRule; //flag to change for queries, to change functionality of Predicate function
    unsigned int leftParens; //counter for expressions to know when they end
    string buffer; //used to store expressions
    
public:
    vector<Token> list;
    
    Parser(vector<Token> myList){
        index = 0;
        list = myList;
        isRule = true;
        leftParens = 0;
        buffer = "";
    }
    
    void Parse(){
        try{
            DatalogProgram_();
        }
        catch(int e){
            cout << "Failure!" << endl;
            cout << "  " << list.at(index) << endl;
            return;
        }
        return;
    }
    
    
    
    void DatalogProgram_(){
        if (list.at(index).getType() == "SCHEMES"){
            index++;
            
            if (list.at(index).getType() == "COLON"){
                index++;
                b.setID(list.at(index).getVal());
                
                Scheme();
                
                SchemeList();
                
                if (list.at(index).getType() == "FACTS"){
                    index++;
                    
                    if (list.at(index).getType() == "COLON"){
                        index++;
                        
                        FactList();
                        
                        if (list.at(index).getType() == "RULES"){
                            index++;
                            
                            if (list.at(index).getType() == "COLON"){
                                index++;
                                
                                RuleList();
                                
                                if (list.at(index).getType() == "QUERIES"){
                                    isRule = false;
                                    index++;
                                    
                                    if (list.at(index).getType() == "COLON"){
                                        index++;
                                        
                                        Query();
                                        
                                        QueryList();
                                        
                                        cout << "Success!" << endl;
                                        cout << prog.toString();
                                        
                                        
                                        cout << "Domain(" << dom.size() << "):" << endl;
                                        set <string>::iterator itr;
                                        for (itr = dom.begin(); itr != dom.end(); ++itr)
                                        {
                                            cout << "  " << *itr << endl;
                                        }
                                        
                                        return;
                                        
                                    }
                                    else throw 0;
                                    
                                }
                                else throw 0;
                                
                            }
                            else throw 0;
                            
                        }
                        else throw 0;
                        
                    }
                    else throw 0;
                    
                }
                else throw 0;
                
            }
            else throw 0;
            
        }
        else throw 0;
        
        
        
        
        return;
        
        
        
    }
    
    
    //FOLLOW sets: FACTS
    //FIRST sets: ID
    void SchemeList(){
        
        if (list.at(index).getType() == "FACTS"){ //lambda check
            return;
        }
        b.setID(list.at(index).getVal());
        
        Scheme();
        
        SchemeList();
        
        return;
        
    }
    
    
    //FOLLOW sets: RULES
    //FIRST sets: ID
    void FactList(){
        b.Reset();
        if (list.at(index).getType() == "RULES"){ //lambda check
            return;
        }
        b.setID(list.at(index).getVal());
        
        Fact();
        
        FactList();
        
        return;
        
    }
    
    
    //FOLLOW sets: QUERIES
    //FIRST sets: ID
    void RuleList(){
        
        if (list.at(index).getType() == "QUERIES"){
            return;
        }
        
        Rule_();
        
        RuleList();
        
        return;
        
    }
    
    
    //FOLLOW sets: EOF
    //FIRST sets: ID
    void QueryList(){
        
        if (list.at(index).getType() == "EOF"){ //lambda check for EOF
            return;
        }
        
        Query();
        
        QueryList();
        
        return;
        
    }
    
    
    void Scheme(){
        
        if (list.at(index).getType() == "ID"){
            index++;
            
            if (list.at(index).getType() == "LEFT_PAREN"){
                index++;
                
                if (list.at(index).getType() == "ID"){
                    b.addParameter(list.at(index).getVal());
                    index++;
                    IDList();
                    
                    if (list.at(index).getType() == "RIGHT_PAREN"){
                        prog.addScheme(b);
                        b.Reset();
                        index++;
                        return;
                        
                    }
                    else throw 0;
                    
                }
                else throw 0;
                
            }
            else throw 0;
                
        }
        else throw 0;
        return;
    }
    
    
    void Fact(){
        
        if (list.at(index).getType() == "ID"){
            index++;
            
            if (list.at(index).getType() == "LEFT_PAREN"){
                index++;
                
                if (list.at(index).getType() == "STRING"){
                    b.addParameter(list.at(index).getVal());
                    InsertDomain(list.at(index).getVal());
                    index++;
                    
                    StringList();
                    
                    if (list.at(index).getType() == "RIGHT_PAREN"){
                        prog.addFact(b);
                        index++;
                        
                        if (list.at(index).getType() == "PERIOD"){
                            index++;
                            return;
                            
                        }
                        else throw 0;
                        
                    }
                    else throw 0;
                    
                }
                else throw 0;
                
            }
            else throw 0;
            
        }
        else throw 0;
        return;
    }
    
    
    
    void Rule_(){
        
        HeadPredicate();
        
        if (list.at(index).getType() == "COLON_DASH"){
            index++;
            
            Predicate_();
            
            PredicateList();
            
            if (list.at(index).getType() == "PERIOD"){
                prog.addRule(r);
                r.reset();
                index++;
                return;
                
            }
            else throw 0;
            
        }
        else throw 0;
        return;
    }
    
    
    
    void Query(){
        
        Predicate_();
        
        if (list.at(index).getType() == "Q_MARK"){
            prog.addQuery(b);
            b.Reset();
            index++;
            return;
            
        }
        else throw 0;
        return;
    }
    
    
    
    void HeadPredicate(){
        
        b.setID(list.at(index).getVal());
        if (list.at(index).getType() == "ID"){
            index++;
            
            if (list.at(index).getType() == "LEFT_PAREN"){
                index++;
                
                if (list.at(index).getType() == "ID"){
                    b.addParameter(list.at(index).getVal());
                    index++;
                    
                    IDList();
                    
                    if (list.at(index).getType() == "RIGHT_PAREN"){
                        r.addHead(b);
                        b.Reset();
                        index++;
                        return;
                        
                    }
                    else throw 0;
                    
                }
                else throw 0;
                
            }
            else throw 0;
            
        }
        else throw 0;
        return;
    }
    
    
    
    void Predicate_(){
        
        if (list.at(index).getType() == "ID"){
            b.setID(list.at(index).getVal());
            index++;
            
            if (list.at(index).getType() == "LEFT_PAREN"){
                index++;
                
                Parameter();
                
                ParameterList();
                
                if (list.at(index).getType() == "RIGHT_PAREN"){
                    if (isRule){
                        r.addPred(b);
                        b.Reset();
                    }
                    
                    
                    index++;
                    return;
                    
                }
                
            }
            else throw 0;
            
        }
        else throw 0;
        
        
        return;
    }
    
    
    
    //FOLLOW sets: PERIOD
    //FIRST sets: COMMA
    void PredicateList(){
        
        if (list.at(index).getType() == "PERIOD"){ //lambda check
            return;
        }
        
        if (list.at(index).getType() == "COMMA"){
            index++;
            
            Predicate_();
            
            PredicateList();
            
            return;
            
        }
        else throw 0;
        return;
    }
    
    
    
    //FOLLOW sets: RIGHT_PAREN
    //FIRST sets: COMMA
    void ParameterList(){
        
        if (list.at(index).getType() == "RIGHT_PAREN"){ //lambda check
            return;
        }
        
        if (list.at(index).getType() == "COMMA"){
            index++;
            
            Parameter();
            
            ParameterList();
            
            return;
            
        }
        else throw 0;
        return;
        
    }
    
    
    
    //FOLLOW sets: RIGHT_PAREN
    //FIRST sets: COMMA
    void StringList(){
        
        if (list.at(index).getType() == "RIGHT_PAREN"){ //lambda check
            return;
        }
        
        if (list.at(index).getType() == "COMMA"){
            index++;
            
            if (list.at(index).getType() == "STRING"){
                b.addParameter(list.at(index).getVal());
                InsertDomain(list.at(index).getVal());
                index++;
                StringList();
                return;
                
            }
            else throw 0;
            
        }
        else throw 0;
        return;
        
    }
    
    
    
    //FOLLOW sets: RIGHT_PAREN
    //FIRST sets: COMMA
    void IDList(){
        
        if (list.at(index).getType() == "RIGHT_PAREN"){ //lambda check
            return;
        }
        
        if (list.at(index).getType() == "COMMA"){
            index++;
            b.addParameter(list.at(index).getVal());
            
            if (list.at(index).getType() == "ID"){
                index++;
                IDList();
                return;
                
            }
            else throw 0;
            
        }
        else throw 0;
        return;
    }
    
    
    
    void Parameter(){
        
        if (list.at(index).getType() == "STRING" || list.at(index).getType() == "ID"){ //if it is an ID or a string
            if (leftParens == 0){
                b.addParameter(list.at(index).getVal());
                index++;
                return;
            }
            else{
                buffer += list.at(index).getVal();
                index++;
                return;
            }
            
            
        }
        else if (list.at(index).getType() == "LEFT_PAREN"){ //if it is an expression
            Expression();
            return;
            
        }
        else throw 0;
        return;
    }
    
    //(X+(Y*C))
    
    //(X+(Y*C))
    
    void Expression(){
        
        if (list.at(index).getType() == "LEFT_PAREN"){
            buffer += "(";
            leftParens++;
            index++;
            
            Parameter();
            
            Operator();
            
            Parameter();
            
            if (list.at(index).getType() == "RIGHT_PAREN"){
                leftParens--;
                buffer += ")";
                if (leftParens == 0){
                    b.addParameter(buffer);
                    buffer = "";
                }
                index++;
                return;
                
            }
            else throw 0;
            
        }
        else throw 0;
        return;
    }
    
    
    void Operator(){
        
        if (list.at(index).getType() == "MULTIPLY"){
            buffer += "*";
            index++;
            return;
            
        }
        else if (list.at(index).getType() == "ADD"){
            buffer += "+";
            index++;
            return;
            
        }
        else throw 0;
        return;
    }
    
    void InsertDomain(string s){
        dom.insert(s);
        return;
    }
    
    
    
};





#endif /* Parser_h */
