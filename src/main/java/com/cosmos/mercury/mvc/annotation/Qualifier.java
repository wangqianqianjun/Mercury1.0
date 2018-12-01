package com.cosmos.mercury.mvc.annotation;

import java.lang.annotation.*;

/**
 * @author wangqianjun
 * Created by wangqianjun on 2018/12/1.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Qualifier {

    String value() default "";
}
