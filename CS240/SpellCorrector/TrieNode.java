package spell;

public class TrieNode implements INode{
    public TrieNode[] nodes;
    public int count;

    public TrieNode(){
        nodes = new TrieNode[26];
        count = 0;
    }

    TrieNode at(char c){
        return nodes[c-'a'];
    }

    void addNode(char c){
        nodes[c-'a'] = new TrieNode();
    }

    @Override
    public int getValue() {
        return count;
    }

    @Override
    public boolean equals(Object o){
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != this.getClass()) return false;
        if (count != ((TrieNode) o).getValue()) return false;

        for (int i = 0; i < 26; i++){
            if (((TrieNode) o).nodes[i] != null && nodes[i] == null){
                return false;
            }
            if (((TrieNode) o).nodes[i] == null && nodes[i] != null){
                return false;
            }
        }
        return true;
    }


}
