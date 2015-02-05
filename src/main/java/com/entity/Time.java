package com.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 记录行情的时间
 *
 * @author hao
 */
@Entity
@Table(name = "tb_time")
public class Time extends BaseEntity {

    public Time() {
    }

    public Time(int time) {
        this.time = time;
    }

    private int time;


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
