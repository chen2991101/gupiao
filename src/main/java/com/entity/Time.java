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

    private int macdStatus;//macd金叉的状态 0：未初始化 1：正在初始化 2：初始化完成
    private int kdjStatus;//kdj金叉的状态 0：未初始化 1：正在初始化 2：初始化完成

    public int getKdjStatus() {
        return kdjStatus;
    }

    public void setKdjStatus(int kdjStatus) {
        this.kdjStatus = kdjStatus;
    }

    public int getMacdStatus() {
        return macdStatus;
    }

    public void setMacdStatus(int macdStatus) {
        this.macdStatus = macdStatus;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
