package com.zy.Hash;
import java.util.HashSet;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        int a = 39;
        System.out.println(((Integer)a).hashCode());

        int b = -39;
        System.out.println(((Integer)b).hashCode());

        double c = 3.1415926;
        System.out.println(((Double)c).hashCode());

        String d = "imooc";
        System.out.println(d.hashCode());

        Student student = new Student(3, 2, "bobo", "liu");
        System.out.println(student.hashCode());

        HashSet<Student> set = new HashSet<>();
        set.add(student);

        HashMap<Student, Integer> scores = new HashMap<>();
        scores.put(student, 100);

        Student student1 = new Student(3, 3, "BOBO", "liu");
        System.out.println(student1.hashCode());
    }
}
