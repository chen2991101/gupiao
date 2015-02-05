package com.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 访问的ip地址
 *
 * @author hao
 */
@Entity
@Table(name = "tb_ip")
public class IpAddress extends BaseEntity {

    private String country;//国家
    private String province;//省份
    private String city;//城市;
    private String ip;//ip地址
    private int time;//时间

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
