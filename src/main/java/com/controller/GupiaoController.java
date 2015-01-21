package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2015-01-20.
 */
@Controller
public class GupiaoController {

    @RequestMapping("chen")
    public void chen() {

        System.out.println("你好");
    }
}
