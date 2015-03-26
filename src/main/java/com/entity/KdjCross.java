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

    public KdjCross(String no, String name, int time) {
        this.no = no;
        this.name = name;
        this.time = time;
    }

    private String no;
    private String name;
    private int time;

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