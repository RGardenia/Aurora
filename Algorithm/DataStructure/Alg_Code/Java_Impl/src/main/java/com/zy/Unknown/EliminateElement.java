package com.zy.Unknown;

public class EliminateElement {

    public static class ListNode {
      int val;
      ListNode next;
      ListNode(int x) { val = x; }

      //链表结点构造函数
        public ListNode(int[] arr){

          if(arr == null || arr.length == 0)
              throw new IllegalArgumentException("arr can not be empty!");

          this.val = arr[0];
          ListNode cur = this;
          for(int i = 1; i< arr.length ; i++){
              cur.next = new ListNode(arr[i]);
              cur = cur.next;
          }
        }
    }

    /*public ListNode removeElements(ListNode head, int val) {

        while(head != null && head.val == val){   //开始部分需删除
            ListNode delNode = head;
            head = head.next;
            delNode.next = null;
        }

        if(head == null)
            return null;

        ListNode prev = head;
        while(prev.next != null){
            if(prev.next.val == val){
                ListNode delNode = prev.next;
                prev.next = delNode.next;
                delNode.next = null;
            }else{
                prev = prev.next;
            }
        }

        return head;
    }*/

    //虚拟头结点方法
    /*public ListNode removeElements(ListNode head, int val) {

        ListNode dummyHead = new ListNode(-1);
        dummyHead.next = head;

        ListNode prev = dummyHead;
        while(prev.next != null){
            if(prev.next.val == val){
                ListNode delNode = prev.next;
                prev.next = delNode.next;
                delNode.next = null;
            }else{
                prev = prev.next;
            }
        }

        return dummyHead.next;
    }*/

    //递归Method
    public ListNode removeElements(ListNode head, int val, int depth){

        String depthString = generateDepthString(depth);
        System.out.print(depthString);
        System.out.println("Call: remove " + val + " in " + head);

        if(head == null){
            System.out.print(depthString);
            System.out.println("Return: " + null);
            return null;
        }

        ListNode res  = removeElements(head.next, val, depth + 1);

        System.out.println(depthString);
        System.out.println("After remove " + val + ": " + res);

        ListNode ret;
        if(head.val == val)
            ret = res;
        else{
            head.next = res;
            ret = head;
        }

        System.out.println(depthString);
        System.out.println("Return: " + ret);
        return ret;

        //return head.val == val ? head.next : head;
        /*if(head.val == val)
            return head.next;
        else
            return head;*/
    }

    private String generateDepthString(int depth){
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < depth; i++)
            res.append("--");
        return res.toString();
    }


    public static void main(String[] args) {
        //测试链表（normal，虚拟头结点， 递归）method的销毁指定元素

        int[] nums = {1, 2, 6, 3, 4, 5, 6};

        ListNode head = new ListNode(nums);
        System.out.println(head);

        ListNode res = (new EliminateElement()).removeElements(head, 6, 0);
        System.out.println(res);
    }
}
