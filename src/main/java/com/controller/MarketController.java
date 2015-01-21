package com.controller;

import com.Utils;
import com.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 股票的controller
 * Created by Administrator on 2015-01-20.
 */
@Controller
@RequestMapping("market")
public class MarketController {

    @Autowired
    MarketService marketService;

    /**
     * 添加上海的股票
     */
    @RequestMapping(value = "addSh", produces = Utils.textutf8)
    @ResponseBody
    public void addSh() {
        marketService.addMarket(true);
    }
}
