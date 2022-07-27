

#ifndef DatalogProgram_h
#define DatalogProgram_h


#include "Predicate.h"
#include "Rule.h"
#include <iostream>
#include <vector>

using namespace std;


class DatalogProgram{
    
private:
    vector<Predicate> schemeList;
    vector<Predicate> factList;
    vector<Rule> ruleList;
    vector<Predicate> queryList;
    
public:
    
    void addScheme(Predicate toAdd){
        schemeList.push_back(toAdd);
    }
    
    void addFact(Predicate toAdd){
        factList.push_back(toAdd);
    }
    
    void addRule(Rule toAdd){
        ruleList.push_back(toAdd);
    }
    
    void addQuery(Predicate toAdd){
        queryList.push_back(toAdd);
    }
    
    Predicate getScheme(size_t index){
        return schemeList.at(index);
    }
    
    Predicate getFact(size_t index){
        return factList.at(index);
    }
    
    Rule getRule(size_t index){
        return ruleList.at(index);
    }
    
    Predicate getQuery(size_t index){
        return queryList.at(index);
    }
    
    size_t numSchemes(){
        return schemeList.size();
    }
    
    size_t numFacts(){
        return factList.size();
    }
    
    size_t numRules(){
        return ruleList.size();
    }
    
    size_t numQueries(){
        return queryList.size();
    }
    
    string toString(){
        string toReturn = "";
        toReturn += "Schemes(";
        toReturn += to_string(schemeList.size());
        toReturn += "):\n";
        for (size_t i = 0; i < schemeList.size(); i++){
            toReturn += "  ";
            toReturn += schemeList.at(i).toString();
            toReturn += "\n";
        }
        
        toReturn += "Facts(";
        toReturn += to_string(factList.size());
        toReturn += "):\n";
        for (size_t i = 0; i < factList.size(); i++){
            toReturn += "  ";
            toReturn += factList.at(i).toString();
            toReturn += "\n";
        }
        
        toReturn += "Rules(";
        toReturn += to_string(ruleList.size());
        toReturn += "):\n";
        for (size_t i = 0; i < ruleList.size(); i++){
            toReturn += "  ";
            toReturn += ruleList.at(i).toString();
            toReturn += "\n";
        }
        
        toReturn += "Queries(";
        toReturn += to_string(queryList.size());
        toReturn += "):\n";
        for (size_t i = 0; i < queryList.size(); i++){
            toReturn += "  ";
            toReturn += queryList.at(i).toString();
            toReturn += "?\n";
        }
        
        return toReturn;
    }
    
    
    
    
};





#endif /* DatalogProgram_h */
