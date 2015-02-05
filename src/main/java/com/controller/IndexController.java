package com.controller;

import com.Utils;
import com.service.IpAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
