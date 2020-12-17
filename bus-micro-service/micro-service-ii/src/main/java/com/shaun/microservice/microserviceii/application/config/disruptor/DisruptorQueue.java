package com.shaun.microservice.microserviceii.application.config.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.Iterator;
import java.util.List;

public class DisruptorQueue<T> {
    private Disruptor<DisruptorEvent<T>> disruptor;
    private RingBuffer<DisruptorEvent<T>> ringBuffer;

    public DisruptorQueue(Disruptor<DisruptorEvent<T>> disruptor) {
        this.disruptor = disruptor;
        this.ringBuffer = disruptor.getRingBuffer();
        this.disruptor.start();
    }

    public void post(T t) {
        if (t != null) {
            long sequence = this.ringBuffer.next();

            try {
                DisruptorEvent<T> event = (DisruptorEvent) this.ringBuffer.get(sequence);
                event.setObj(t);
            } finally {
                this.ringBuffer.publish(sequence);
            }
        }
    }

    public void post(List<T> ts) {
        if (ts != null) {
            Iterator<T> var2 = ts.iterator();

            while (var2.hasNext()) {
                T t = var2.next();
                if (t != null) {
                    this.post(t);
                }
            }
        }
    }

    public long cursor() {
        return this.disruptor.getRingBuffer().getCursor();
    }

    public void shutdown() {
        this.disruptor.shutdown();
    }

    public Disruptor<DisruptorEvent<T>> getDisruptor() {
        return this.disruptor;
    }

    public void setDisruptor(Disruptor<DisruptorEvent<T>> disruptor) {
        this.disruptor = disruptor;
    }

    public RingBuffer<DisruptorEvent<T>> getRingBuffer() {
        return this.ringBuffer;
    }

    public void setRingBuffer(RingBuffer<DisruptorEvent<T>> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }
}