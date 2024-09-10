package com.atguigu.bigdata.sparksql;

import java.io.Serializable;
import java.util.Map;

public class MyCityRemarkBuffer implements Serializable {
    private Long count;
    private Map<String, Long> cityMap = null;

    public MyCityRemarkBuffer(Long count, Map<String, Long> cityMap) {
        this.count = count;
        this.cityMap = cityMap;
    }

    public MyCityRemarkBuffer() {
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Map<String, Long> getCityMap() {
        return cityMap;
    }

    public void setCityMap(Map<String, Long> cityMap) {
        this.cityMap = cityMap;
    }
}
