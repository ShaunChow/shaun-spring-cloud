package com.shaun.microservice.microserviceii.application.config.disruptor;


import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

public abstract class DisruptorConsumer<T> implements EventHandler<DisruptorEvent<T>>, WorkHandler<DisruptorEvent<T>> {
    public DisruptorConsumer() {
    }

    public void onEvent(DisruptorEvent<T> event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);
    }

    public void onEvent(DisruptorEvent<T> event) throws Exception {
        this.consume(event.getObj());
    }

    public abstract void consume(T var1);
}