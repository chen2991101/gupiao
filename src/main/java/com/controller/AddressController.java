package com.controller;

import com.Utils;
import com.entity.Address;
import com.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 位置服务的controller
 * Created by hao on 2015/1/24.
 */
@Controller
public class AddressController {
    @Autowired
    AddressService addressService;


    /**
     * 上传位置
     *
     * @return
     */
    @RequestMapping(value = "uploadAddress", produces = Utils.textutf8)
    @ResponseBody
    public String uploadAddress(Address address) {
        addressService.uploadAddress(address);
        return "{success:true}";
    }
}
