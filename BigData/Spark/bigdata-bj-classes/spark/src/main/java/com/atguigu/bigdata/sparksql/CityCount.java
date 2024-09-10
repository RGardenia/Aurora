package com.atguigu.bigdata.sparksql;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class CityCount implements Serializable, Comparable<CityCount> {
    private String cityName;
    private Long count;

    public CityCount(String cityName, Long count) {
        this.cityName = cityName;
        this.count = count;
    }

    public CityCount() {
    }

    @Override
    public int compareTo(@NotNull CityCount other) {
//        return (int)(other.count - this.count);
        if ( this.count < other.count ) {
            return 1;
        } else if ( this.count > other.count ) {
            return -1;
        } else {
            return 0;
        }
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
