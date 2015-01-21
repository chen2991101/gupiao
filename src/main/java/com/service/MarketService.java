package com.service;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Service;

/**
 * 操作股票的service
 * Created by Administrator on 2015-01-21.
 */
@Service
public class MarketService {

    /**
     * 添加股票信息
     *
     * @param isSh 是否是上海的股票
     */
    public void addMarket(boolean isSh) {
        String type;//股票的类型
        int marketNo;//股票代码
        int maxNo;//最大查询到的代码
        if (isSh) {
            type = "sh";
            marketNo = 600000;
            maxNo = 604000;
        } else {
            type = "sz";
            marketNo = 0;
            maxNo = 4000;
        }
        HttpClient httpClient = new DefaultHttpClient();//httpclient请求
        String query = "s_" + type + String.format("%06d", marketNo);//请求的查询条件

    }
}
