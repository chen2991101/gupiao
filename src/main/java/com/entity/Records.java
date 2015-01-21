package com.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 每天的行情
 *
 * @author hao
 */
@Entity
@Table(name = "tb_records")
public class Records extends BaseEntity {

    public Records() {
        super();
    }

    public Records(String no, String name, BigDecimal currentPrice) {
        this.no = no;
        this.currentPrice = currentPrice;
        this.name = name;
    }

    private String name;// 名称 1
    private String no;// 代码 2
    private BigDecimal currentPrice;// 当前价格 3
    private BigDecimal yesterday_income;// 昨收 4
    private BigDecimal today_open; // 今开 5
    private BigDecimal deal; // 交易 6
    private int out_dish;// 外盘 7
    private int in_dish;// 内盘 8
    private BigDecimal buy1;// 买一 9
    private float buy1l;// 买一量 10
    private BigDecimal buy2;// 买二11
    private float buy2l;// 买二量 12
    private BigDecimal buy3;// 买三 13
    private float buy3l;// 买三量 14
    private BigDecimal buy4;// 买四 15
    private float buy4l;// 买四量 16
    private BigDecimal buy5;// 买五 17
    private float buy5l;// 买五量 18

    private BigDecimal sale1;// 卖一 19
    private float sale1l;// 卖一量 20
    private BigDecimal sale2;// 卖二 21
    private float sale2l;// 卖二量 22
    private BigDecimal sale3;// 卖三 13
    private float sale3l;// 卖三量 24
    private BigDecimal sale4;// 卖四 25
    private float sale4l;// 卖四量 26
    private BigDecimal sale5;// 卖五27
    private float sale5l;// 卖五量 28

    private int time;// 时间 30
    private BigDecimal upanddown;// 涨跌 31
    private float upanddown2;// 涨跌% 32
    private BigDecimal heightest; // 最高 33
    private BigDecimal lowest;// 最低 34

    private BigDecimal dealAmount;// 成交额 37
    private float handover;// 换手率% 38
    private float pe;// 市盈率% 39

    private float zf;// 振幅 43
    private BigDecimal ltsz;// 流通市值 44
    private BigDecimal totalMoney;// 总市值 45
    private float sjl;// 市净率 46
    private BigDecimal ztj;// 涨停价 47
    private BigDecimal dtj;// 跌停价 48

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

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getYesterday_income() {
        return yesterday_income;
    }

    public void setYesterday_income(BigDecimal yesterday_income) {
        this.yesterday_income = yesterday_income;
    }

    public BigDecimal getToday_open() {
        return today_open;
    }

    public void setToday_open(BigDecimal today_open) {
        this.today_open = today_open;
    }

    public BigDecimal getDeal() {
        return deal;
    }

    public void setDeal(BigDecimal deal) {
        this.deal = deal;
    }

    public int getOut_dish() {
        return out_dish;
    }

    public void setOut_dish(int out_dish) {
        this.out_dish = out_dish;
    }

    public int getIn_dish() {
        return in_dish;
    }

    public void setIn_dish(int in_dish) {
        this.in_dish = in_dish;
    }

    public BigDecimal getBuy1() {
        return buy1;
    }

    public void setBuy1(BigDecimal buy1) {
        this.buy1 = buy1;
    }

    public float getBuy1l() {
        return buy1l;
    }

    public void setBuy1l(float buy1l) {
        this.buy1l = buy1l;
    }

    public BigDecimal getBuy2() {
        return buy2;
    }

    public void setBuy2(BigDecimal buy2) {
        this.buy2 = buy2;
    }

    public float getBuy2l() {
        return buy2l;
    }

    public void setBuy2l(float buy2l) {
        this.buy2l = buy2l;
    }

    public BigDecimal getBuy3() {
        return buy3;
    }

    public void setBuy3(BigDecimal buy3) {
        this.buy3 = buy3;
    }

    public float getBuy3l() {
        return buy3l;
    }

    public void setBuy3l(float buy3l) {
        this.buy3l = buy3l;
    }

    public BigDecimal getBuy4() {
        return buy4;
    }

    public void setBuy4(BigDecimal buy4) {
        this.buy4 = buy4;
    }

    public float getBuy4l() {
        return buy4l;
    }

    public void setBuy4l(float buy4l) {
        this.buy4l = buy4l;
    }

    public BigDecimal getBuy5() {
        return buy5;
    }

    public void setBuy5(BigDecimal buy5) {
        this.buy5 = buy5;
    }

    public float getBuy5l() {
        return buy5l;
    }

    public void setBuy5l(float buy5l) {
        this.buy5l = buy5l;
    }

    public BigDecimal getSale1() {
        return sale1;
    }

    public void setSale1(BigDecimal sale1) {
        this.sale1 = sale1;
    }

    public float getSale1l() {
        return sale1l;
    }

    public void setSale1l(float sale1l) {
        this.sale1l = sale1l;
    }

    public BigDecimal getSale2() {
        return sale2;
    }

    public void setSale2(BigDecimal sale2) {
        this.sale2 = sale2;
    }

    public float getSale2l() {
        return sale2l;
    }

    public void setSale2l(float sale2l) {
        this.sale2l = sale2l;
    }

    public BigDecimal getSale3() {
        return sale3;
    }

    public void setSale3(BigDecimal sale3) {
        this.sale3 = sale3;
    }

    public float getSale3l() {
        return sale3l;
    }

    public void setSale3l(float sale3l) {
        this.sale3l = sale3l;
    }

    public BigDecimal getSale4() {
        return sale4;
    }

    public void setSale4(BigDecimal sale4) {
        this.sale4 = sale4;
    }

    public float getSale4l() {
        return sale4l;
    }

    public void setSale4l(float sale4l) {
        this.sale4l = sale4l;
    }

    public BigDecimal getSale5() {
        return sale5;
    }

    public void setSale5(BigDecimal sale5) {
        this.sale5 = sale5;
    }

    public float getSale5l() {
        return sale5l;
    }

    public void setSale5l(float sale5l) {
        this.sale5l = sale5l;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public BigDecimal getUpanddown() {
        return upanddown;
    }

    public void setUpanddown(BigDecimal upanddown) {
        this.upanddown = upanddown;
    }

    public float getUpanddown2() {
        return upanddown2;
    }

    public void setUpanddown2(float upanddown2) {
        this.upanddown2 = upanddown2;
    }

    public BigDecimal getHeightest() {
        return heightest;
    }

    public void setHeightest(BigDecimal heightest) {
        this.heightest = heightest;
    }

    public BigDecimal getLowest() {
        return lowest;
    }

    public void setLowest(BigDecimal lowest) {
        this.lowest = lowest;
    }

    public BigDecimal getDealAmount() {
        return dealAmount;
    }

    public void setDealAmount(BigDecimal dealAmount) {
        this.dealAmount = dealAmount;
    }

    public float getHandover() {
        return handover;
    }

    public void setHandover(float handover) {
        this.handover = handover;
    }

    public float getPe() {
        return pe;
    }

    public void setPe(float pe) {
        this.pe = pe;
    }

    public float getZf() {
        return zf;
    }

    public void setZf(float zf) {
        this.zf = zf;
    }

    public BigDecimal getLtsz() {
        return ltsz;
    }

    public void setLtsz(BigDecimal ltsz) {
        this.ltsz = ltsz;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public float getSjl() {
        return sjl;
    }

    public void setSjl(float sjl) {
        this.sjl = sjl;
    }

    public BigDecimal getZtj() {
        return ztj;
    }

    public void setZtj(BigDecimal ztj) {
        this.ztj = ztj;
    }

    public BigDecimal getDtj() {
        return dtj;
    }

    public void setDtj(BigDecimal dtj) {
        this.dtj = dtj;
    }

}
