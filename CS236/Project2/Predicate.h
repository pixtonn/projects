
#ifndef Predicate_h
#define Predicate_h

#include <vector>



using namespace std;
class Predicate{
    
private:
    
    string ID;
    vector<string> parameters;
    
public:
    
    
    Predicate(string s, vector<string> p){
        ID = s;
        parameters = p;
    }
    
    Predicate(string s){
        ID = s;
    }
    
    Predicate(){
        
    }
    
    void Reset(){
        ID = "";
        vector<string> v;
        parameters = v;
    }
    
    void addParameter(string s){
        parameters.push_back(s);
    }
    
    void setID(string s){
        ID = s;
    }
    
    string getID(){
        return ID;
    }
    
    string toString(){
        string toReturn = "";
        
        toReturn += ID;
        toReturn += "(";
        
        for (size_t i = 0; i < parameters.size() - 1; i++){
            toReturn += parameters.at(i);
            toReturn += ",";
        }
        toReturn += parameters.at(parameters.size() - 1);
        toReturn += ")";
        
        return toReturn;
    }
    
};




#endif /* Predicate_h */
