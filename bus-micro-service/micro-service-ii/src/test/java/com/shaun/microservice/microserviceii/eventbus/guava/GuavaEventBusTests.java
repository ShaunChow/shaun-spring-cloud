package com.shaun.microservice.microserviceii.eventbus.guava;

import com.google.common.eventbus.AsyncEventBus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class GuavaEventBusTests {

    @Resource
    AsyncEventBus asyncEventBus;

    @Test
    void contextLoads() {
        asyncEventBus.post("Guava Event Bus Message!!!");
    }
}
