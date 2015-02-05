package com.service;

import com.Utils;
import com.alibaba.fastjson.JSONObject;
import com.dao.IpAddressDao;
import com.entity.IpAddress;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ip位置service
 * Created by hao on 2015/1/24.
 */
@Service
public class IpAddressService {

    @Autowired
    IpAddressDao ipAddressDao;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    /**
     * 添加ip地址
     *
     * @param ip
     */
    public void addIp(String ip) {
        HttpClient httpClient = new DefaultHttpClient();//httpclient请求
        HttpGet method = new HttpGet("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=" + ip);

        try {
            HttpResponse response = httpClient.execute(method);
            String str = Utils.inputStream2String(response.getEntity().getContent());
            IpAddress address = JSONObject.parseObject(str, IpAddress.class);
            address.setIp(ip);
            address.setTime(Integer.parseInt(dateFormat.format(new Date())));
            ipAddressDao.save(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (method != null) {
            method.releaseConnection();
        }
    }

}
