package com.cosmos.mercury.mvc.annotation;

import java.lang.annotation.*;

/**
 * Created by wangqianjun on 2018/12/1.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface RequestMapping {

    /**
     * 表示访问该方法的url
     * @return
     */
    String value() default "";
}
