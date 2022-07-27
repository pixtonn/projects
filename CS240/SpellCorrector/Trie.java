package spell;

import java.util.Objects;

public class Trie implements ITrie{

    public TrieNode root;
    private int nodeCount;
    private int wordCount;

    public Trie(){
        root = new TrieNode();
        nodeCount = 1;
        wordCount = 0;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o.getClass() != this.getClass()) return false;
        o = (Trie)o;
        if (nodeCount == 1 && ((Trie) o).getNodeCount() == 1) return true;
        bool r = new bool();
        r.val = true;
        equalsHelper(root, ((Trie) o).root, r);
        return r.val;
    }

    private void equalsHelper(TrieNode curr1, TrieNode curr2, bool r){
        if (!curr1.equals(curr2)){
            r.val = false;
            return;
        }
        for (int i = 0; i < 26; i++){
            if (curr1.nodes[i] != null && curr2.nodes[i] != null){
                equalsHelper(curr1.nodes[i], curr2.nodes[i], r);
            }
        }
        return;
    }

    private void toStringHelper(TrieNode n, StringBuilder output, StringBuilder currentWord){
        if (n == null){
            return;
        }
        if (n.getValue() > 0){
            //System.out.println("Adding " + currentWord.toString());
            output.append(currentWord.toString() + "\n");
        }
        TrieNode[] children = n.nodes;
        for (int i = 0; i < 26; i++){
            TrieNode child = children[i];
            if (child != null){
                char c = (char)('a' + i);
                //System.out.println(c);
                currentWord.append(c);
                toStringHelper(child, output, currentWord);
                currentWord.deleteCharAt(currentWord.length() - 1);
            }
        }
        return;
    }

    @Override
    public String toString(){
        StringBuilder output = new StringBuilder();
        StringBuilder currentWord = new StringBuilder();
        toStringHelper(root, output, currentWord);
        System.out.println(output.toString());
        return output.toString();
    }

    @Override
    public int hashCode() {
        int r = nodeCount * wordCount;
        for (int i = 0; i < 26; i++){
            if (root.nodes[i] != null){
                r = r * i;
            }
        }
        return r;
    }

    @Override
    public void add(String word) {
        TrieNode currentNode = root;
        for (int i = 0; i < word.length(); i++){
            if (currentNode.at(word.charAt(i)) == null){
                currentNode.addNode(word.charAt(i));
                //System.out.println("Adding new node of letter: " + word.charAt(i));
                nodeCount++;
            }
            currentNode = currentNode.at(word.charAt(i));
        }
        currentNode.count++;
        if (currentNode.count == 1) {
            wordCount++;
        }
    }

    @Override
    public INode find(String word) {

        if (word == null){
            return null;
        }

        TrieNode currentNode = root;
        for (int i = 0; i < word.length(); i++){

            if (currentNode.at(word.charAt(i)) == null){
                return null;
            }
            currentNode = currentNode.at(word.charAt(i));

        }

        if (currentNode == root || currentNode.count == 0){
            return null;
        }
        return currentNode;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }
}
