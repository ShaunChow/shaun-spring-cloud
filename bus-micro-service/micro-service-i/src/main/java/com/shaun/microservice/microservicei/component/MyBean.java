package com.shaun.microservice.microservicei.component;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("myBean")
public class MyBean {

    @Value("${greeting}")
    private String say;

    public String saySomething() {
        return say + "-" + LocalDateTime.now().toString();
    }

}