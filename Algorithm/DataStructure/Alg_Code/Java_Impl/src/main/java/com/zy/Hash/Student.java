package com.zy.Hash;

public class Student {

    int grate;
    int cls;
    String firstName;
    String lastName;

    public Student(int grate, int cls, String firstName, String lastName){
        this.cls = cls;
        this.firstName = firstName;
        this.grate = grate;
        this.lastName = lastName;
    }

    @Override    //覆盖hashCode函数后， 其对象的hash值一样
    public int hashCode(){
        int B = 31;

        int hash = 0;
        hash = hash * B + grate;
        hash = hash * B + cls;
        hash = hash * B + firstName.toLowerCase().hashCode();
        hash = hash * B + lastName.toLowerCase().hashCode();

        return hash;
    }

    @Override
    public boolean equals(Object o){

        if(this == o)
            return true;

        if(o == null)
            return false;

        if(getClass() != o.getClass())
            return false;

        Student another = (Student)o;
        return this.grate == another.grate &&
                this.cls == another.cls &&
                this.firstName.toLowerCase().equals(another.firstName.toLowerCase()) &&
                this.lastName.toLowerCase().equals(another.lastName.toLowerCase());
    }
}
