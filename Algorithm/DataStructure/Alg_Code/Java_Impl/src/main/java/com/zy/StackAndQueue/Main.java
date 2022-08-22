package com.zy.StackAndQueue;

import java.util.Random;
import java.util.Stack;

public class Main {

    //匹配括号
    public boolean isValid(String s){

        Stack<Character> stack = new Stack<>();
        for(int i = 0; i < s.length() ; i++){
            char c = s.charAt(i);
            if(c == '(' || c == '[' || c == '{')
                stack.push(c);
            else {
                if(stack.isEmpty())
                    return false;

                char topChar = stack.pop();
                if(c == ')' && topChar != '(')
                    return false;
                if(c == ']' && topChar != '[')
                    return false;
                if(c == '}' && topChar != '{')
                    return false;
            }
        }
        return stack.isEmpty();
    }

    private static double test(Queue<Integer> q, int opCount){

        long start = System.nanoTime();
        Random random = new Random();
        for(int i = 0 ; i<opCount; i++)
            q.enqueue(random.nextInt(Integer.MAX_VALUE));
        for(int i = 0; i< opCount; i++)
            q.dequeue();
        long end = System.nanoTime();
        return (end - start) / 1000000000.0;
    }


    public static void main(String[] args) {

        /*ArrayStack<Integer> stack = new ArrayStack<>();

        for(int i = 0; i < 5; i++){
            stack.push(i);
            System.out.println(stack);
        }

        stack.pop();
        System.out.println(stack);*/

        /*System.out.println((new Main()).isValid("()[]{}"));
        System.out.println((new Main()).isValid("()[}"));*/


        int opCount = 100000;

        ArrayQueue<Integer> arrayQueue = new ArrayQueue<>();
        double time = test(arrayQueue,opCount);
        System.out.println(time);

        LoopQueue<Integer> LoopQueue = new LoopQueue<>();
        double time1 = test(LoopQueue,opCount);
        System.out.println(time1);
    }
}
