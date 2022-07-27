
#ifndef Relation_h
#define Relation_h
#include "Tuple.h"
#include <vector>
#include <string>
#include <set>

using namespace std;

class Relation{
private:
    string name;
    vector<string> attributeNames;
    set<Tuple> tuples;
    
    
public:
    Relation(string n){
        name = n;
    }
    Relation(){
        
    }
    
    void addAttribute(string newAttribute){
        attributeNames.push_back(newAttribute);
        return;
    }
    
    Tuple getTuple(size_t index){
        set<Tuple>::iterator itr = tuples.begin();
        for (size_t i = 0; i < index; i++){
            itr++;
        }
        
        return *itr;
        
    }
    
    set<Tuple> getTuples(){
        return tuples;
    }
    
    vector<string> getAttributes(){
        return attributeNames;
    }
    
    string getName(){
        return name;
    }
    
    void setName(string n){
        name = n;
        return;
    }
    
    string getAttribute(size_t index){
        return attributeNames.at(index);
    }
    
    void addTuple(const Tuple tuple){
        tuples.insert(tuple);
        return;
    }
    
    void setAttribute(size_t index, string newName){
        attributeNames.at(index) = newName;
        return;
    }
    
    string toString(){
        string s = "";
        s += "\n";
        for (size_t j = 0; j < attributeNames.size(); j++){
            s += attributeNames.at(j);
            s += " ";
        }
        s += "\n";
        for (size_t j = 0; j < tuples.size(); j++){
            s += this->getTuple(j).toString();
            s += " ";
            s += "\n";
        }
        s += "\n\n";
        
        
        return s;
    }
    
};




#endif /* Relation_h */
