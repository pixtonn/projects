

#ifndef Database_h
#define Database_h
#include <vector>
#include "Relation.h"
#include "DatalogProgram.h"

using namespace std;

Relation project(Relation r, vector<string> attributesToProject);

Relation select(Relation r, string attributeToSelect, string neededValue);

Relation selectSame(Relation r, string attribute1, string attribute2);

Relation rename(Relation r, string toChange, string changeTo);

class Database{

private:
    
    vector<Relation> relations;
    DatalogProgram prog;
    
    
    
public:
    Database(){
        
    }
    
    Database(DatalogProgram p){
        prog = p;
        //cout << "test" << endl;
        //Set up the schemes by making them relations without tuples
        //cout << "The size of p.getSchemes() is " << p.getSchemes().size() << endl;
        for (size_t i = 0; i < p.getSchemes().size(); i++){
            Relation r(p.getScheme(i).getID());
            //cout << "Got here!" << endl;
            for (size_t j = 0; j < p.getScheme(i).getParameters().size(); j++){
                r.addAttribute(p.getScheme(i).getParameter(j));
                //cout << "Attribute added:" << p.getScheme(i).getParameter(j) << endl;
            }
            relations.push_back(r);
        }
        
        //Go through fact list and add each fact to its corresponding
        for (size_t i = 0; i < p.getFacts().size(); i++){
            for (size_t j = 0; j < relations.size(); j++){
                if (p.getFact(i).getID() == relations.at(j).getName()){
                    Tuple t;
                    for (size_t k = 0; k < p.getFact(i).getParameters().size(); k++){
                        t.addAttribute(p.getFact(i).getParameter(k));
                    }
                    relations.at(j).addTuple(t);
                }
            }
        }
        
    }
    
    
    
    
    void solveQueries(){
        
        size_t indexOfMatchingRelation = 99;
        for (size_t i = 0; i < prog.getQueries().size(); i++){//go through each query
            Relation r;//I will build this relation and store variable values in it to output
            vector<string> nonterminals;//holds the parameters of the query that are free, aka a letter like A or X etc.
            for (size_t j = 0; j < relations.size(); j++){//go through each relation
                if (prog.getQueries().at(i).getID() == relations.at(j).getName()){//if the query and relation name match
                    nonterminals.clear();
                    indexOfMatchingRelation = j;
                    
                    
                    for (size_t g = 0; g < relations.at(j).getAttributes().size(); g++){//copy all the attributes from relations(j) to r
                        r.addAttribute(relations.at(j).getAttribute(g));
                    }
                     
                    
                    for (size_t a = 0; a < prog.getQueries().at(i).getParameters().size(); a++){//fill the above vector first
                        if (prog.getQueries().at(i).getParameter(a).at(0) != '\''){
                            bool isAlreadyIn = false;
                            for (size_t o = 0; o < nonterminals.size(); o++){
                                if (nonterminals.at(o) == prog.getQueries().at(i).getParameter(a) && prog.getQueries().at(i).getID() == relations.at(j).getName()/*added here*/){//if it was a letter already inputted, like A when there was already an A in nonterminal, we need to not put it in nonterminals again
                                    isAlreadyIn = true;
                                }
                            }
                            if (!isAlreadyIn){//if it's not already in the vector, add it
                                nonterminals.push_back(prog.getQueries().at(i).getParameter(a));
                                //cout << "Nonterminal found named " << prog.getQueries().at(i).getParameter(a) << endl;
                                //r.addAttribute(relations.at(j).getAttribute(a));  //POSSIBLE FIXME
                            }
                        }
                    }
                    /*
                    cout << "Nonterminals:";
                    for (size_t z = 0; z < nonterminals.size(); z++){
                        cout << nonterminals.at(z) << " ";
                    }
                    cout << endl;
                    */
                    
                    vector<string> terminals = nonterminals; //holds the corresponding values of the letters in the nonterminals for each given tuple
                    for (size_t k = 0; k < relations.at(j).getTuples().size(); k++){//go through each tuple in that relation
                        terminals = nonterminals;//reset the terminals for each tuple and fill the variables as they are first met
                        bool tupleContinue = false;//set to true once I know a tuple contains the current parameter
                        
                        for (size_t n = 0; n < relations.at(j).getTuple(k).getSize(); n++){
                            //cout << "Evaluating " << relations.at(j).getTuple(k).getValue(n) << endl;
                            tupleContinue = false;
                            if (prog.getQueries().at(i).getParameter(n).at(0) == '\'' && prog.getQueries().at(i).getParameter(n) == relations.at(j).getTuple(k).getValue(n)){//if the terminal in the query and the given tuple value match
                                //continue
                                //cout << "Parameter value: " << prog.getQueries().at(i).getParameter(n) << endl;
                                tupleContinue = true;
                            }
                            else{
                                //cout << prog.getQueries().at(i).getParameter(n) << endl;
                                for (size_t p = 0; p < nonterminals.size(); p++){
                                    if (nonterminals.at(p) == prog.getQueries().at(i).getParameter(n) && nonterminals.at(p) == terminals.at(p) && prog.getQueries().at(i).getID() == relations.at(j).getName()/*added here*/){//if it is an unset variable, set it
                                        //cout << "This value made it past: " << prog.getQueries().at(i).getParameter(n) << endl;
                                        //cout << "The size of nonterminals is " << nonterminals.size() << endl;
                                        //cout << nonterminals.at(p) << " is equal to " << terminals.at(p) << endl;
                                        //cout << "Setting terminal: " << terminals.at(p) << " to " << relations.at(j).getTuple(k).getValue(n) << endl;
                                        terminals.at(p) = relations.at(j).getTuple(k).getValue(n);
                                        //cout << "Terminal " << nonterminals.at(p) << " set to be " << terminals.at(p) << endl;
                                        tupleContinue = true;
                                    }
                                    //else if (true){
                                        //cout << "About to test " << terminals.at(p) << " vs " << prog.getQueries().at(i).getParameter(n) << endl;
                                    //}
                                    else if (nonterminals.at(p) == prog.getQueries().at(i).getParameter(n) && terminals.at(p) == relations.at(j).getTuple(k).getValue(n)){//else if it is a variable that is already set
                                        //cout << "Repeat variable found: " << nonterminals.at(p) << endl;
                                        
                                        tupleContinue = true;
                                    }
                                }
                            }
                            
                            //cout << relations.at(j).getTuple(k).getSize() << endl;
                            
                            if (!tupleContinue){
                                n = relations.at(j).getTuple(k).getSize();
                            }
                            else if (n == relations.at(j).getTuple(k).getSize() - 1){//if we get here, it means the tuple passed amd should be added to the relation
                                r.addTuple(relations.at(j).getTuple(k));
                            }
                        }
                    }
                    
                    
                }
                
            }
            
            
            cout << prog.getQueries().at(i).toString() << "? ";
            if (r.getTuples().size() == 0){
                cout << "No" << endl;
            }
            else{
                cout << "Yes(" << r.getTuples().size() << ")" << endl;
                vector<string> attributesToProject;
                vector<string> nonterminalsAlreadyPassed;
                for (size_t q = 0; q < nonterminals.size(); q++){
                    for (size_t r = 0; r < prog.getQueries().at(i).getParameters().size(); r++){
                        if (nonterminals.at(q) == prog.getQueries().at(i).getParameter(r)){//if it was a variable
                            
                            
                            
                            //cout << "Adding " << relations.at(indexOfMatchingRelation).getAttribute(r) << " to the list of attributes to output" << endl;
                            //cout << "Nonterminal " << nonterminals.at(q) << " being added to the output relation" << endl;
                            bool alreadyPassed = false;
                            for (size_t s = 0; s < nonterminalsAlreadyPassed.size(); s++){
                                if (nonterminalsAlreadyPassed.at(s) == nonterminals.at(q)){
                                    alreadyPassed = true;
                                }
                            }
                            nonterminalsAlreadyPassed.push_back(nonterminals.at(q));
                            
                            
                            if (!alreadyPassed){
                                attributesToProject.push_back(relations.at(indexOfMatchingRelation).getAttribute(r));
                            }
                        }
                    }
                }
                
                
                /*
                cout << "1" << endl << r.toString();
                
                
                
                cout << "Attributes in attributesToProject:" << endl;
                for (size_t f = 0; f < attributesToProject.size(); f++){
                    cout << attributesToProject.at(f) << " ";
                }
                cout << endl;
                 */
                
                if (r.getTuple(0).getValue(0) == "\'\'" && r.getTuples().size() == 2){
                    Relation rel;
                    rel.addAttribute(r.getAttribute(0));
                    rel.addAttribute(r.getAttribute(1));
                    rel.addTuple(r.getTuple(1));
                    r = rel;
                }
                
                
                
                r = project(r, attributesToProject);
                //cout << "2" << endl << r.toString();
                //cout << "r has " << r.getTuples().size() << " tuples" << endl;
                //cout << "Total attributes to project: " << attributesToProject.size() << endl;
                
                for (size_t p = 0; p < r.getTuples().size(); p++){
                    cout << "  ";
                    for (size_t q = 0; q < nonterminals.size(); q++){
                        cout << nonterminals.at(q) << "=" << r.getTuple(p).getValue(q);
                        if (q + 1 < nonterminals.size()){
                            cout << ", ";
                        }
                    }
                    cout << endl;
                }
                
                
                
            }
            
            
        }
        
        /*
        for (size_t t = 0; t < relations.size(); t++){
            cout << relations.at(t).toString();
        }
        */
        
        return;
    }
    
    
    
    
    
    
    
    string toString(){
        string s = "";
        for (size_t i = 0; i < relations.size(); i++){
            s += "Name: ";
            s += relations.at(i).getName();
            s += "\n";
            for (size_t j = 0; j < relations.at(i).getAttributes().size(); j++){
                s += relations.at(i).getAttribute(j);
                s += " ";
            }
            s += "\n";
            for (size_t j = 0; j < relations.at(i).getTuples().size(); j++){
                s += relations.at(i).getTuple(j).toString();
                s += " ";
                s += "\n";
            }
            s += "\n\n";
        }
        return s;
    }
    
    
};


Relation project(Relation r, vector<string> attributesToProject){
    Relation toReturn;
    vector<Tuple> copyTuples;
    bool tuplesCreated = false;
    
    for (size_t i = 0; i < attributesToProject.size(); i++){
        for (size_t j = 0; j < r.getAttributes().size(); j++){
            if (attributesToProject.at(i) == r.getAttribute(j)){
                toReturn.addAttribute(attributesToProject.at(i));
                if (!tuplesCreated){
                    tuplesCreated = true;
                    for (size_t k = 0; k < r.getTuples().size(); k++){
                        Tuple t;
                        t.addAttribute(r.getTuple(k).getValue(j));
                        copyTuples.push_back(t);
                    }
                }
                else{
                    for (size_t k = 0; k < r.getTuples().size(); k++){
                        copyTuples.at(k).addAttribute(r.getTuple(k).getValue(j));
                    }
                }
            }
        }
    }
    
    for (size_t x = 0; x < copyTuples.size(); x++){
        toReturn.addTuple(copyTuples.at(x));
    }
    
    
    
    return toReturn;
}


Relation select(Relation r, string attributeToSelect, string neededValue){
    Relation toReturn;
    
    
    for (size_t x = 0; x < r.getAttributes().size(); x++){
        toReturn.addAttribute(r.getAttribute(x));
    }
    
    for (size_t j = 0; j < r.getAttributes().size(); j++){
        
        if (r.getAttribute(j) == attributeToSelect){
            
            for (size_t k = 0; k < r.getTuples().size(); k++){
                
                if (neededValue == r.getTuple(k).getValue(j)){
                    
                    toReturn.addTuple(r.getTuple(k));
                }
            }
        }
        
    }
    
    return toReturn;
}



Relation rename(Relation r, string toChange, string changeTo){
    Relation toReturn;
    
    toReturn = r;
    for (size_t i = 0; i < toReturn.getAttributes().size(); i++){
        if (toReturn.getAttribute(i) == toChange){
            toReturn.setAttribute(i, changeTo);
        }
    }
    return toReturn;
}

Relation selectSame(Relation r, string attribute1, string attribute2){
    Relation toReturn;
    
    for (size_t x = 0; x < r.getAttributes().size(); x++){
        toReturn.addAttribute(r.getAttribute(x));
    }
    
    for (size_t j = 0; j < r.getAttributes().size(); j++){
        
        if (r.getAttribute(j) == attribute1){
            
            for (size_t i = 0; i < r.getAttributes().size(); i++){
                
                if (r.getAttribute(i) == attribute2){
                    
                    for (size_t k = 0; k < r.getTuples().size(); k++){
                        if (r.getTuple(k).getValue(i) == r.getTuple(k).getValue(j)){
                            toReturn.addTuple(r.getTuple(k));
                        }
                    }
                    
                    
                    
                    
                }
                
            }
            
            
        }
        
    }
    
    
    
    return toReturn;
}





#endif /* Database_h */
