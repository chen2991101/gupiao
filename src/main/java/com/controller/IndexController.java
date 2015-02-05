package com.controller;

import com.Utils;
import com.alibaba.fastjson.JSONObject;
import com.service.IpAddressService;
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                ipService.addIp(request.getRemoteAddr());
            }
        }).start();
    }


    @RequestMapping(value = "findIp", produces = Utils.textutf8)
    @ResponseBody
    public String findRecords(int page, int rows) {
        return JSONObject.toJSONString(ipService.findIp(page, rows));
    }
}
