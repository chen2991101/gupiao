package com.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 股票KDJ指标
 *
 * @author hao
 */
@Entity
@Table(name = "tb_kdj")
public class Kdj extends BaseEntity {

    public Kdj() {
    }

    public Kdj(String no, String name, int time, BigDecimal k, BigDecimal d, BigDecimal j) {
        this.no = no;
        this.name = name;
        this.time = time;
        this.k = k;
        this.d = d;
        this.j = j;
    }

    private String no;//股票代码
    private String name;//股票名称
    private BigDecimal k;
    private BigDecimal d;
    private BigDecimal j;
    private int time;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
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
}
