
#include "Relation.h"
#include <vector>
#include <iostream>

Relation project(Relation r, vector<string> attributesToProject);

Relation select(Relation r, string attributeToSelect, string neededValue);

Relation selectSame(Relation r, string attribute1, string attribute2);

Relation rename(Relation r, string toChange, string changeTo);

int main(int argc, const char * argv[]) {
    
    /*
    
    Relation r("SK");
    r.addAttribute("A1");
    r.addAttribute("A2");
    Tuple t;
    t.addAttribute("aa");
    t.addAttribute("bb");
    r.addTuple(t);
    Tuple n;
    n.addAttribute("aa");
    n.addAttribute("dd");
    r.addTuple(n);
    Tuple p;
    p.addAttribute("ee");
    p.addAttribute("ff");
    r.addTuple(p);
    vector<string> projector;
    projector.push_back("A1");
    
    cout << r.toString() << "is being projected on " << endl;
    for (size_t i = 0; i < projector.size(); i++){
        cout << projector.at(i) << endl;
    }
    
    cout << endl << endl;
    cout << project(r, projector).toString();
    
    cout << r.toString() << "is being renamed " << endl;
    string toChange = "A1";
    string changeTo = "1A";
    cout << toChange << "->" << changeTo << endl << endl;
    cout << rename(r, toChange, changeTo).toString();
    
    cout << endl << endl;
    
    
    Relation rel("ABC");
    rel.addAttribute("X1");
    rel.addAttribute("Y2");
    rel.addAttribute("Z3");
    Tuple a;
    a.addAttribute("gg");
    a.addAttribute("ff");
    a.addAttribute("gg");
    rel.addTuple(a);
    Tuple b;
    b.addAttribute("yy");
    b.addAttribute("yy");
    b.addAttribute("xx");
    rel.addTuple(b);
    vector<string> pro;
    pro.push_back("X1");
    pro.push_back("Z3");
    
    cout << rel.toString() << "is being projected on " << endl;
    for (size_t i = 0; i < pro.size(); i++){
        cout << pro.at(i) << endl;
    }
    
    cout << endl << endl;
    
    cout << project(rel, pro).toString();
    
    
    cout << rel.toString() << "is being selected" << endl;
    
    string attributeToSelect;
    attributeToSelect = "X1";
    //attributesToSelect.push_back("Y2");
    //attributesToSelect.push_back("Z3");
    string neededValue;
    neededValue = "gg";
    cout << attributeToSelect << "=" << neededValue << endl;
    
    
    
    cout << select(rel, attributeToSelect, neededValue).toString();
    
    
    
    cout << endl << rel.toString() << "is being selected same" << endl;
    
    string attribute1 = "X1";
    string attribute2 = "Z3";
    
    cout << selectSame(rel, attribute1, attribute2).toString();
    
    */
    
    Relation beta("beta");
    beta.addAttribute("cat");
    beta.addAttribute("fish");
    beta.addAttribute("bird");
    beta.addAttribute("bunny");
    Tuple t0;
    t0.addAttribute("3");
    t0.addAttribute("4");
    t0.addAttribute("2");
    t0.addAttribute("4");
    beta.addTuple(t0);
    Tuple t1;
    t1.addAttribute("6");
    t1.addAttribute("4");
    t1.addAttribute("9");
    t1.addAttribute("2");
    beta.addTuple(t1);
    Tuple t2;
    t2.addAttribute("4");
    t2.addAttribute("3");
    t2.addAttribute("2");
    t2.addAttribute("7");
    beta.addTuple(t2);
    Tuple t3;
    t3.addAttribute("1");
    t3.addAttribute("5");
    t3.addAttribute("2");
    t3.addAttribute("4");
    beta.addTuple(t3);
    Tuple t4;
    t4.addAttribute("1");
    t4.addAttribute("5");
    t4.addAttribute("8");
    t4.addAttribute("3");
    beta.addTuple(t4);
    
    cout << "Original beta relation:" << beta.toString();
    
    
    cout << "Projection of 'bunny' on beta" << endl;
    vector<string> attri;
    attri.push_back("bunny");
    cout << project(beta, attri).toString();
    
    
    cout << "Projection of 'bunny', 'bird', and 'cat' on beta" << endl;
    attri.clear();
    attri.push_back("bunny");
    attri.push_back("bird");
    attri.push_back("cat");
    cout << project(beta, attri).toString();
    
    cout << "Renaming 'bunny' to  'tiger'" << endl;
    cout << rename(beta, "bunny", "tiger").toString();
    
    cout << "Selecting 'cat=1' on beta" << endl;
    cout << select(beta, "cat", "1").toString();
    
    cout << "Selecting 'fish' = 'bunny' on beta" << endl;
    cout << selectSame(beta, "fish", "bunny").toString();
    
    
    
    Relation alpha("alpha");
    alpha.addAttribute("cat");
    alpha.addAttribute("dog");
    alpha.addAttribute("fish");
    Tuple t5;
    t5.addAttribute("1");
    t5.addAttribute("2");
    t5.addAttribute("5");
    alpha.addTuple(t5);
    Tuple t6;
    t6.addAttribute("1");
    t6.addAttribute("4");
    t6.addAttribute("1");
    alpha.addTuple(t6);
    Tuple t7;
    t7.addAttribute("2");
    t7.addAttribute("3");
    t7.addAttribute("2");
    alpha.addTuple(t7);
    Tuple t8;
    t8.addAttribute("6");
    t8.addAttribute("7");
    t8.addAttribute("4");
    alpha.addTuple(t8);
    
    cout << "Original alpha:" << endl;
    cout << alpha.toString();
    
    cout << "Projection of 'fish' and 'dog' of alpha" << endl;
    attri.clear();
    attri.push_back("fish");
    attri.push_back("dog");
    cout << project(alpha, attri).toString();
    
    cout << "Renaming 'fish' to 'walrus' in alpha" << endl;
    cout << rename(alpha, "fish", "walrus").toString();
    
    cout << "Selecting  'cat' = 1 in alpha" << endl;
    cout << select(alpha, "cat", "1").toString();
    
    
    
    Relation childOf("childOf");
    childOf.addAttribute("A");
    childOf.addAttribute("B");
    Tuple g0;
    g0.addAttribute("Anna");
    g0.addAttribute("Dan");
    childOf.addTuple(g0);
    Tuple g1;
    g1.addAttribute("Anna");
    g1.addAttribute("Kim");
    childOf.addTuple(g1);
    
    cout << childOf.toString();
    
    vector<string> attributes;
    attributes.push_back("B");
    
    childOf = project(childOf, attributes);
    
    cout << childOf.toString();
    
    
    
    
    
    return 0;
}

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
