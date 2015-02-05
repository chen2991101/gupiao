package com.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 股票MACD
 *
 * @author hao
 */
@Entity
@Table(name = "tb_macd")
public class Macd extends BaseEntity {

    public Macd() {
    }

    public Macd(int time) {
        this.time = time;
    }

    private BigDecimal ema12;
    private BigDecimal ema26;
    private BigDecimal diff;
    private BigDecimal dea;
    private BigDecimal bar;
    private String no;
    private String name;
    private int time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public BigDecimal getEma12() {
        return ema12;
    }

    public void setEma12(BigDecimal ema12) {
        this.ema12 = ema12;
    }

    public BigDecimal getEma26() {
        return ema26;
    }

    public void setEma26(BigDecimal ema26) {
        this.ema26 = ema26;
    }

    public BigDecimal getDiff() {
        return diff;
    }

    public void setDiff(BigDecimal diff) {
        this.diff = diff;
    }

    public BigDecimal getDea() {
        return dea;
    }

    public BigDecimal getBar() {
        return bar;
    }

    public void setBar(BigDecimal bar) {
        this.bar = bar;
    }

    public void setDea(BigDecimal dea) {
        this.dea = dea;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
