package com.cosmos.mercury.mvc.controller;

import com.cosmos.mercury.mvc.annotation.Controller;
import com.cosmos.mercury.mvc.annotation.Qualifier;
import com.cosmos.mercury.mvc.annotation.RequestMapping;
import com.cosmos.mercury.mvc.service.TestService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wangqianjun on 2018/12/1.
 */
@Controller
@RequestMapping("test")
public class TestController {

    @Qualifier("testService")
    private TestService testService;

    @RequestMapping("/we-are-champ")
    private String test(HttpServletRequest req, HttpServletResponse resp){
        testService.test();
        return "hello";
    }

    @RequestMapping("/wex")
    private String wex(HttpServletRequest req, HttpServletResponse resp){
        testService.test();
        return "hello";
    }
}
