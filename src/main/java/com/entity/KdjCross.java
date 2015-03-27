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
@Table(name = "tb_kdj_cross")
public class KdjCross extends BaseEntity {


    public KdjCross() {
    }

    public KdjCross(String no, String name, int time,BigDecimal k,BigDecimal d,BigDecimal j) {
        this.no = no;
        this.name = name;
        this.time = time;
        this.k=k;
        this.d=d;
        this.j=j;
    }

    private String no;
    private String name;
    private int time;
    private BigDecimal k;
    private BigDecimal d;
    private BigDecimal j;

    public BigDecimal getK() {
        return k;
    }

    public void setK(BigDecimal k) {
        this.k = k;
    }

    public BigDecimal getD() {
        return d;
    }

    public void setD(BigDecimal d) {
        this.d = d;
    }

    public BigDecimal getJ() {
        return j;
    }

    public void setJ(BigDecimal j) {
        this.j = j;
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