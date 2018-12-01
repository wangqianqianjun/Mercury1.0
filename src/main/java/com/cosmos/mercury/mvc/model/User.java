package com.cosmos.mercury.mvc.model;

import java.io.Serializable;

/**
 * Created by wangqianjun on 2018/12/1.
 */
public class User implements Serializable{
    private static final long serialVersionUID = 4344600204386450786L;

    private String name;

    private Integer age;

    public User() {
    }

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
