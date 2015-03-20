package com.controller;

import com.Utils;
import com.alibaba.fastjson.JSONObject;
import com.entity.Address;
import com.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 位置服务的controller
 * Created by hao on 2015/1/24.
 */
@Controller
@RequestMapping(value = "address", produces = Utils.textutf8)
public class AddressController {
    @Autowired
    AddressService addressService;

    /**
     * 上传位置
     *
     * @return
     */
    @RequestMapping(value = "uploadAddress")
    @ResponseBody
    public String uploadAddress(Address address) {
        addressService.uploadAddress(address);
        return "{success:true}";
    }


    /**
     * 查询地址信息
     *
     * @return
     */
    @RequestMapping(value = "find")
    @ResponseBody
    public String find(int page, int rows) {
        return JSONObject.toJSONString(addressService.find(page, rows));
    }
}
