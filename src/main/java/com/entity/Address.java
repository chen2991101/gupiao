package com.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 记账app的用户地址
 *
 * @author hao
 */
@Entity
@Table(name = "tb_address")
public class Address extends BaseEntity {

    private String address;//地址信息
    private double latitude;// 纬度
    private double longitude;// 经度
    private String deviceId;// 设置Id
    private float radius;//定位精度
    private String model;//手机型号

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

}
