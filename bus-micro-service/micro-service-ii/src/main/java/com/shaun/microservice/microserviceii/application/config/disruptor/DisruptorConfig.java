package com.shaun.microservice.microserviceii.application.config.disruptor;

import com.shaun.microservice.microserviceii.application.config.eventbus.LocalEventListening;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DisruptorConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public DisruptorQueue tcpEventBusRegister() {


        DisruptorQueue disruptorQueue = DisruptorQueueFactory.getHandleEventsQueue(
                4,
                false,
                applicationContext.getBeansOfType(DisruptorConsumer.class)
                        .values()
                        .toArray(new DisruptorConsumer[0]));

        return disruptorQueue;
    }
}