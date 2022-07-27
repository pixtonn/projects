
#ifndef Token_h
#define Token_h
#include <string>
#include <fstream>
using namespace std;

class Token{
private:
    string type;
    string value;
    int line;
    
public:
    Token(string typeSet, string valueSetter, int lineNum){
        value = valueSetter;
        type = typeSet;
        line = lineNum;
    }
    string getVal(){
        return value;
    }
    string getType(){
        return type;
    }
    int getLine(){
        return line;
    }
    
    
    
    friend ostream& operator<<(ostream& os, const Token& t){
        os << "(" << t.type << ",\"" << t.value << "\"," << t.line << ")";
        return os;
    }
    
};




#endif /* Token_h */
