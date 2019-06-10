package com.aop.aopdemo.rest;

import com.aop.aopdemo.annotation.LoginLimit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AopController {

    @RequestMapping("/m1")
    public void expressionMethod(String username,String pawd){
        System.out.println("m1 方法执行=");
    }
    @LoginLimit
    @RequestMapping("/m2")
    public void annotationMethod(){
        System.out.println("m2 方法执行=");
    }
}
