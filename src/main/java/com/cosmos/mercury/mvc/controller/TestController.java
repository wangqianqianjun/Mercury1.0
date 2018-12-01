package com.cosmos.mercury.mvc.controller;

import com.cosmos.mercury.mvc.annotation.Controller;
import com.cosmos.mercury.mvc.annotation.Qualifier;
import com.cosmos.mercury.mvc.annotation.RequestMapping;
import com.cosmos.mercury.mvc.annotation.ResponseBody;
import com.cosmos.mercury.mvc.common.RequestMethod;
import com.cosmos.mercury.mvc.model.User;
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

    @RequestMapping(value = "/test1")
    @ResponseBody
    private Object test(HttpServletRequest req, HttpServletResponse resp){
        String name=req.getParameter("name");
        String age=req.getParameter("age");
        System.out.println(name+":"+age);
        testService.test();
        return new User(name,123);
    }

    @RequestMapping(value = "/test2" ,method = RequestMethod.GET)
    @ResponseBody
    private String wex(HttpServletRequest req, HttpServletResponse resp){
        String wiki=req.getParameter("wiki");
        System.out.println(wiki);
        testService.test();
        return wiki;
    }

    @RequestMapping(value = "/test3")
    @ResponseBody
    private String noMethod(HttpServletRequest req, HttpServletResponse resp){
        String wiki=req.getParameter("wiki");
        System.out.println(wiki);
        testService.test();
        return wiki;
    }
}
