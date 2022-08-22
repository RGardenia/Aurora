package com.zy.Trie;
import java.util.TreeMap;

/**
 * 前缀树
 *
 *  https://cdn.jsdelivr.net/gh/Rainbow503/PicGo/img/QQ图片20220329103334.png
 */
public class Trie {
    private class Node{
        public boolean isWord;
        public int value;
        public TreeMap<Character, Node> next;

        public Node(boolean isWord){
            this.isWord = isWord;
            next = new TreeMap<>();
        }

        public Node(int value){
            this.value = value;
            next = new TreeMap<>();
        }

        public Node(int value, boolean isWord){
            this.value = value;
            this.isWord = isWord;
            next = new TreeMap<>();
        }

        public Node(){
            this(0, false);
        }
    }

    private Node root;
    private int size;

    public Trie(){
        root = new Node();
        size = 0;
    }

    public int getSize(){
        return size;
    }

    //向Trie中添加一个新的单词
    public void add(String word){

        Node cur = root;
        for(int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            if(cur.next.get(c) == null)
                cur.next.put(c, new Node());
            cur = cur.next.get(c);
        }

        if(!cur.isWord) {
            cur.isWord = true;
            size++;
        }
    }

    //查询一个单词是否存在
    public boolean contains(String word){

        Node cur = root;
        for(int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            if(cur.next.get(c) == null)
                return false;
            cur = cur.next.get(c);
        }
        return cur.isWord;
    }

    //查找是否Trie有单词以prefix为前缀
    public boolean isPrefix(String prefix){

        Node cur = root;
        for(int i = 0; i < prefix.length(); i++){
            char c = prefix.charAt(i);
            if(cur.next.get(c) == null)
                return false;
            cur = cur.next.get(c);
        }
        return true;
    }

    //
    public boolean search(String word){

        return match(root, word, 0);
    }

    private boolean match(Node node, String word, int index){

        if(index == word.length())
            return node.isWord;

        char c = word.charAt(index);
        if(c != '.'){
            if(node.next.get(c)  == null)
                return false;
            return match(node.next.get(c), word, index +1 );
        }
        else{
            for(char nextChar: node.next.keySet())
                if(match(node.next.get(nextChar), word, index + 1))
                    return true;
            return false;
        }
    }

    //
    public void insert(String word, int val){

        Node cur = root;
        for(int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            if(cur.next.get(c) == null)
                cur.next.put(c, new Node());
            cur = cur.next.get(c);
        }
        cur.value = val;
    }

    public int sum(String prefix){

        Node cur = root;
        for(int i = 0; i < prefix.length(); i++){
            char c = prefix.charAt(i);
            if(cur.next.get(c) == null)
                return 0;
            cur = cur.next.get(c);
        }
        return sum(cur);
    }

    //遍历以node为根节点的所有值
    private int sum(Node node){

//        if(node.next.size() == 0)
//            return node.value;

        int res = node.value;
        for(char c: node.next.keySet())
            res += sum(node.next.get(c));

        return res;
    }
}
