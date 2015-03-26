package com.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * MACD金叉数据
 *
 * @author hao
 */
@Entity
@Table(name = "tb_macd_cross")
public class MacdCross extends BaseEntity {


    public MacdCross() {
    }

    public MacdCross(BigDecimal diff, String no, String name, int time) {
        this.diff = diff;
        this.no = no;
        this.name = name;
        this.time = time;
    }

    private BigDecimal diff;
    private String no;
    private String name;
    private int time;

    public BigDecimal getDiff() {
        return diff;
    }

    public void setDiff(BigDecimal diff) {
        this.diff = diff;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}