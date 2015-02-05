package com.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * macd需要的行情
 *
 * @author hao
 */
@Entity
@Table(name = "tb_macd_records")
public class MACDRecords extends BaseEntity {

    private String no;// 代码
    private String name;//股票名称
    private BigDecimal open;//开盘价格
    private BigDecimal highest;//最高
    private BigDecimal lowest;//最低
    private BigDecimal price;//收盘价
    private BigDecimal deal;//交易量
    private BigDecimal dealMoney;//交易金额
    private int time;//时间


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

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHighest() {
        return highest;
    }

    public void setHighest(BigDecimal highest) {
        this.highest = highest;
    }

    public BigDecimal getLowest() {
        return lowest;
    }

    public void setLowest(BigDecimal lowest) {
        this.lowest = lowest;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDeal() {
        return deal;
    }

    public void setDeal(BigDecimal deal) {
        this.deal = deal;
    }

    public BigDecimal getDealMoney() {
        return dealMoney;
    }

    public void setDealMoney(BigDecimal dealMoney) {
        this.dealMoney = dealMoney;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
