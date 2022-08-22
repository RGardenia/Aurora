package com.zy.Unknown;
import java.util.TreeSet;

public class Morse {

    public int uniqueMorseRepresentations(String[] words){

        String[] codes = {".-","-...","-.-.","-..",".","..-.","--.","....","..",".---","-.-",".-..","--","-.","---",".--.","--.-",".-.","...","-","..-","...-",".--","-..-","-.--","--.."};

        TreeSet<String> set = new TreeSet<>();
        for(String word: words){

            StringBuilder res = new StringBuilder();
            for(int i = 0; i < words.length; i++)
                res.append(codes[word.charAt(i) - 'a']);
            //一个字符接一个的找到对应的摩斯码，并加上去

            set.add(res.toString());
            //自动忽略重复
        }

        return set.size();
    }
}
