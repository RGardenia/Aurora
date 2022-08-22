package com.zy.Unknown;
import com.zy.StackAndQueue.PriorityQueue;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class Frequent {

    private class Freq implements Comparable<Freq>{
        int e, freq;

        public Freq(int e, int freq){
            this.e = e;
            this.freq = freq;
        }

        //定义优先级
        @Override
        public int compareTo(Freq o) {
            if(this.freq < o.freq)
                return 1;
            else if(this.freq > o.freq)
                return -1;
            else
                return 0;
        }
    }

    public List<Integer> topFrequent(int[] nums, int k){

        TreeMap<Integer, Integer> map = new TreeMap<>();
        //包含元素，记录频次
        for(int num: nums) {
            if (map.containsKey(num))
                map.put(num, map.get(num) + 1);
            else
                map.put(num, 1);
        }

        PriorityQueue<Freq> pq = new PriorityQueue<>();
        for(int key: map.keySet()){
            if(pq.getSize() < k)
                pq.enqueue(new Freq(key, map.get(key)));
            else if(map.get(key) > pq.getFront().freq){
                pq.dequeue();
                pq.enqueue(new Freq(key, map.get(key)));
            }
        }

        LinkedList<Integer> res = new LinkedList<>();
        while(!pq.isEmpty())
            res.add(pq.dequeue().e);
        return res;
    }
}
