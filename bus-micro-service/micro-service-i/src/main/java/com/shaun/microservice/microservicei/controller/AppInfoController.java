package com.shaun.microservice.microservicei.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class AppInfoController {
    @GetMapping("appinfo")
    public Object getInfo() {
        HashMap<String, Object> result = new HashMap<>();


        result.put("name","Shaun Chow");

        return result;
    }
}
