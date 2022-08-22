package com.zy.RedBlackTree;

import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        int n = 20000000;

        Random random = new Random();
        ArrayList<Integer> testData = new ArrayList<>();
        for (int i = 0; i < n; i++)
            testData.add(random.nextInt(Integer.MAX_VALUE));

        //Test BST
        long startTime = System.nanoTime();

        /*BST<Integer, Integer> bst = new BST<>();
        for(Integer x: testData)
            bst.add(x, null);*/

        long endTime = System.nanoTime();
    }
}
