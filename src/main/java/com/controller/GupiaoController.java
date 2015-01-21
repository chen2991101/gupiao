package com.controller;

import com.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 股票的controller
 * Created by Administrator on 2015-01-20.
 */
@Controller
@RequestMapping("market")
public class GupiaoController {

    @Autowired
    MarketService marketService;

    @RequestMapping("chen")
    public void chen() {
        System.out.println("你好");
    }
}
