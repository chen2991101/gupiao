package com.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 股票信息
 *
 * @author hao
 */
@Entity
@Table(name = "tb_market")
public class Market extends BaseEntity {
    public Market() {
    }

    public Market(String no, String name) {
        this.name = name;
        this.no = no;
    }

    private String name;
    private String no;
    private String n;

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

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

}
