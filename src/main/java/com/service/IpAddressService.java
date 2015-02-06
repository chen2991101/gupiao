package com.service;

import com.Utils;
import com.alibaba.fastjson.JSONObject;
import com.dao.IpAddressDao;
import com.dao.TimeDao;
import com.entity.IpAddress;
import com.entity.Time;
import org.apache.commons.collections.map.HashedMap;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * ip位置service
 * Created by hao on 2015/1/24.
 */
@Service
public class IpAddressService {

    @Autowired
    IpAddressDao ipAddressDao;
    @Autowired
    TimeDao timeDao;
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


    /**
     * 查询所有的ip地址
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Map<String, Object> findIp(int pageNo, int pageSize) {
        Map<String, Object> map = new HashedMap();
        Page<IpAddress> page = ipAddressDao.
                findAll(new PageRequest(pageNo - 1, pageSize, new Sort(new Sort.Order(Sort.Direction.DESC, "createDate"))));
        map.put("total", page.getTotalElements());
        map.put("rows", page.getContent());
        return map;
    }
}
