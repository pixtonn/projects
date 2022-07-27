
#ifndef Rule_h
#define Rule_h

#include "Predicate.h"
#include <iostream>

using namespace std;

class Rule{
private:
    Predicate headPred;
    vector<Predicate> r;
    
public:
    
    Rule(Predicate p){
        headPred = p;
    }
    
    Rule(){
        
    }
    
    void addPred(Predicate p){
        r.push_back(p);
    }
    
    void addHead(Predicate p){
        headPred = p;
    }
    
    size_t size(){
        return r.size();
    }
    
    void reset(){
        headPred.Reset();
        r.clear();
    }
    
    string toString(){
        
        string toReturn = "";
        toReturn += headPred.toString();
        
        toReturn += " :- ";
        for (size_t i = 0; i < r.size() - 1; i++){
            toReturn += r.at(i).toString();
            toReturn += ",";
        }
        toReturn += r.at(r.size() - 1).toString();
        toReturn += ".";
        
        return toReturn;
    }
    
    
};







#endif /* Rule_h */
