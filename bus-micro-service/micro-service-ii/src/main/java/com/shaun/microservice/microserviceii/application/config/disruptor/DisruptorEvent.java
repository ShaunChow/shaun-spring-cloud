package com.shaun.microservice.microserviceii.application.config.disruptor;


public class DisruptorEvent<T> {
    private T obj;

    public DisruptorEvent() {
    }

    public T getObj() {
        return this.obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}