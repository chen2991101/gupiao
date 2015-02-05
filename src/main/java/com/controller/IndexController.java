package com.controller;

import com.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 访问首页的controller
 * Created by hao on 2015/1/24.
 */
@Controller
public class IndexController {

    @RequestMapping(value = "index", produces = Utils.textutf8)
    public void index() {
    }
}
