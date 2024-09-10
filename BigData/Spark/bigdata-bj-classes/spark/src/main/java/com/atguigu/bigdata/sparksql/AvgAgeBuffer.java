package com.atguigu.bigdata.sparksql;

import java.io.Serializable;

public class AvgAgeBuffer implements Serializable {
    private Long total;
    private Long cnt;

    public AvgAgeBuffer(Long total, Long cnt) {
        this.total = total;
        this.cnt = cnt;
    }

    public AvgAgeBuffer() {
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getCnt() {
        return cnt;
    }

    public void setCnt(Long cnt) {
        this.cnt = cnt;
    }
}