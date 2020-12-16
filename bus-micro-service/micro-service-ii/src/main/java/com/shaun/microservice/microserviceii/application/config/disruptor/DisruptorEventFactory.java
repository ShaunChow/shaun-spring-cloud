package com.shaun.microservice.microserviceii.application.config.disruptor;


import com.lmax.disruptor.EventFactory;

public class DisruptorEventFactory<T> implements EventFactory<DisruptorEvent<T>> {
    public DisruptorEventFactory() {
    }

    public DisruptorEvent<T> newInstance() {
        return new DisruptorEvent();
    }
}