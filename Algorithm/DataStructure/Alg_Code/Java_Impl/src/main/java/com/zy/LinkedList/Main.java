package com.zy.LinkedList;

public class Main {

    public static void main(String[] args) {
        //测试链表
        /*ChainList<Integer> linkedList = new ChainList<>();

        for(int i = 0; i < 5; i++){
            linkedList.addFirst(i);
            System.out.println(linkedList);
        }

        linkedList.addMiddle(2,666);
        System.out.println(linkedList);

        linkedList.remove(2);
        System.out.println(linkedList);

        linkedList.removeFirst();
        System.out.println(linkedList);

        linkedList.removeLast();
        System.out.println(linkedList);*/



        //测试队列链表
        LinkedListQueue<Integer> queue = new LinkedListQueue<>();
        for(int i = 0; i < 10 ; i++){
            queue.enqueue(i);
            System.out.println(queue);

            if(i % 3 == 2){
                queue.dequeue();
                System.out.println(queue);
            }
        }
    }
}
