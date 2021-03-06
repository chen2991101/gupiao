package com.controller;

import com.Utils;
import com.alibaba.fastjson.JSONObject;
import com.service.IpAddressService;
import com.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 访问首页的controller
 * Created by hao on 2015/1/24.
 */
@Controller
public class IndexController {
    @Autowired
    IpAddressService ipService;

    @RequestMapping(value = "index", produces = Utils.textutf8)
    public void index(final HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");

        if (request.getHeader("x-forwarded-for") == null) {
            ip = request.getRemoteAddr();
        } else {
            ip = ip.split(",")[0];
        }

        if (!ip.equals("127.0.0.1")) {
            final String finalIp = ip;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ipService.addIp(finalIp);
                }
            }).start();
        }
    }


    @RequestMapping(value = "findIp", produces = Utils.textutf8)
    @ResponseBody
    public String findRecords(int page, int rows) {
        return JSONObject.toJSONString(ipService.findIp(page, rows));
    }

}
