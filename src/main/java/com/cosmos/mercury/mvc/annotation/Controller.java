package com.cosmos.mercury.mvc.annotation;

import java.lang.annotation.*;

/**
 * @author wangqianjun
 * Created by wangqianjun on 2018/12/1.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {

    /**
     * 表示给controller的别名
     * @return
     */
    String value() default "";
}
