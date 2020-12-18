package com.shaun.microservice.microserviceii.eventbus.disruptor;

import com.shaun.microservice.microserviceii.application.config.disruptor.DisruptorQueue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class DisruptorTests {

    @Resource
    DisruptorQueue disruptorQueue;

    @Test
    void contextLoads() {
        disruptorQueue.post("Disruptor Event Message!!!");
    }
}
