package com.cosmos.mercury.mvc.service.impl;

import com.cosmos.mercury.mvc.annotation.Service;
import com.cosmos.mercury.mvc.service.TestService;

/**
 * Created by wangqianjun on 2018/12/1.
 */
@Service("testService")
public class TestServiceImpl implements TestService {
    @Override
    public void test() {
        System.out.println("hi you got me!");
    }
}
