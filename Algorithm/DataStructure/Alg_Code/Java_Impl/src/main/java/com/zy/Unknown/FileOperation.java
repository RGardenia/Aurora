package com.zy.Unknown;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class FileOperation {

    //读取filename中的内容，并将其中所有的单词放进words中
    public static boolean readFile(String fileName, ArrayList<String> words){

        if(fileName == null || words == null){
            System.out.println("fileName is null or words is null!");
            return false;
        }

        //读取文件
        Scanner scanner = null;

        try{
            File file = new File(fileName);
            if(file.exists()){
                FileInputStream fis = new FileInputStream(file);
                scanner = new Scanner(new BufferedInputStream(fis), "UTF");
                scanner.useLocale(Locale.ENGLISH);
            }else
                return false;
        }catch (IOException ioe){
            System.out.println("Cannot open " + fileName);
        }

        //只做demo作用
        if(scanner.hasNextLine()){

            String contents = scanner.useDelimiter("\\A").next();

            int start = firstCharacterIndex(contents, 0);
            for(int i = start + 1; i <= contents.length(); )
                if(i == contents.length() || !Character.isLetter(contents.charAt(i))){
                    String word = contents.substring(start, i);
                    words.add(word);
                    start = firstCharacterIndex(contents,i);
                    i = start + 1;
                }else
                    i++;
        }

        return true;
    }

    //寻找字符串s中，从start开始的第一个字母字符发位置
    private static int firstCharacterIndex(String s, int start){

        for( int i = start ; i < s.length(); i++){
            if(Character.isLetter(s.charAt(i)))
                return i;
        }

        return s.length();
    }
}
