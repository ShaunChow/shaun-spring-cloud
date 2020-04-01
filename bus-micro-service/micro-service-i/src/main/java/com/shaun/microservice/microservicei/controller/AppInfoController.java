package com.shaun.microservice.microservicei.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.shaun.microservice.microservicei.entity.Employee;
import com.shaun.microservice.microservicei.feign.MicroServiceIIClient;
import com.shaun.microservice.microservicei.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class AppInfoController {

    @Autowired
    private MicroServiceIIClient microServiceIIClient;

    @Autowired
    private EmployeeMapper employeeMapper;

    @GetMapping("appinfo")
    @HystrixCommand(fallbackMethod = "getInfo_fallback")
    public Object getInfo() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", "Shaun Chow");
        result.put("service_id", "micro-service-i");
        result.put("port", 8801);
        result.put("body", microServiceIIClient.getInfo());

        return result;
    }

    @PostMapping
    public Integer addEmployee(@RequestBody Employee employee) {
        return employeeMapper.insert(employee);
    }

    @GetMapping
    public List<Employee> queryAllEmployee() {
        return employeeMapper.selectAll();
    }


    public Object getInfo_fallback() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("method", "controller");
        result.put("name", "resilence...");
        result.put("service_id", "micro-service-ii");

        return result;
    }
}
