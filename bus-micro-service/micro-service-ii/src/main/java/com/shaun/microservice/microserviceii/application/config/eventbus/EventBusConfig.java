package com.shaun.microservice.microserviceii.application.config.eventbus;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

@Configuration
public class EventBusConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public AsyncEventBus tcpEventBusRegister() {

        AsyncEventBus localEventBus = new AsyncEventBus(Executors.newFixedThreadPool(3));

        applicationContext.getBeansOfType(LocalEventListening.class).values()
                .stream()
                .forEach(localEventBus::register);

        localEventBus.register(new LocalDeadEventListener());

        return localEventBus;
    }
}

