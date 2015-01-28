package com.controller;

import com.Utils;
import com.alibaba.fastjson.JSONObject;
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
    public String addSh() {
        addMarket(true);
        return "添加成功";
    }

    /**
     * 添加深圳的股票
     */
    @RequestMapping(value = "addSz", produces = Utils.textutf8)
    @ResponseBody
    public String addSz() {
        addMarket(false);
        return "添加成功";
    }

    /**
     * 添加记录
     */
    @RequestMapping(value = "addRecords", produces = Utils.textutf8)
    @ResponseBody
    public String addRecords() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                marketService.addRecords();
            }
        }).start();
        return "添加成功";
    }


    /**
     * 添加记录
     */
    @RequestMapping(value = "email", produces = Utils.textutf8)
    @ResponseBody
    public String email() {
        Utils.sendEMail("邮件发送成功");
        return "邮件发送成功";
    }

    /**
     * 获取记录的数量，检查数据库连接是否正确
     *
     * @return
     */
    @RequestMapping(value = "count", produces = Utils.textutf8)
    @ResponseBody
    public String count() {
        return marketService.findRecordsCount() + "";
    }


    /**
     * 查询股票记录
     *
     * @return
     */
    @RequestMapping(value = "findRecords", produces = Utils.textutf8)
    @ResponseBody
    public String findRecords(int page, int rows) {
        return JSONObject.toJSONString(marketService.findRecords(page, rows));
    }

    /**
     * 删除已经退市的股票
     *
     * @return
     */
    @RequestMapping(value = "deleteBack", produces = Utils.textutf8)
    @ResponseBody
    public String deleteBack() {
        marketService.deleteBack();
        return "";
    }


    /**
     * 添加历史
     */
    @RequestMapping(value = "addHistory", produces = Utils.textutf8)
    @ResponseBody
    public String addHistory() {
        marketService.addHistory();
        return "";
    }

    /**
     * 添加股票信息
     *
     * @param isSh 是否是上海的股票
     */
    private void addMarket(final boolean isSh) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                marketService.addMarket(isSh);
            }
        }).start();
    }
}
