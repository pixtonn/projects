
#ifndef Tuple_h
#define Tuple_h

#include <vector>
#include <string>


using namespace std;
class Tuple{
    
protected:
    vector<string> list;
    
public:
    Tuple(){
        
    }
    size_t getSize(){
        return list.size();
    }
    
    void addAttribute(string toAdd){
        list.push_back(toAdd);
        return;
    }
    
    vector<string> getList(){
        return list;
    }
    
    const string getValue(size_t index){
        return list.at(index);
    }
    
    string toString(){
        string s = "";
        for (size_t i = 0; i < list.size(); i++){
            s += list.at(i);
            s += " ";
        }
        return s;
    }
    
    friend bool operator< (const Tuple &left, const Tuple &right);
};

bool operator< (const Tuple &left, const Tuple &right)
{
    vector<string> first = left.list;
    vector<string> second = right.list;
    for (size_t i = 0; i < first.size(); i++){
        if (first.at(i) != second.at(i)){
            return first.at(i) < second.at(i);
        }
    }
    
    return 0 < 0;
}



#endif /* Tuple_h */
