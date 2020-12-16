package com.shaun.microservice.microserviceii.application.config.disruptor;

import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;

public class DisruptorQueueFactory {

    // 创建"点对电模式"的操作队列，即同一事件会被一组消费者其中之一消费
    public static <T> DisruptorQueue<T> getWorkPoolQueue(int queueSize, boolean isMoreProducer,
                                                         DisruptorConsumer<T>... consumers) {
        Disruptor<DisruptorEvent<T>> _disruptor = new Disruptor(new DisruptorEventFactory(),
                queueSize, Executors.defaultThreadFactory(),
                isMoreProducer ? ProducerType.MULTI : ProducerType.SINGLE,
                new SleepingWaitStrategy());
        _disruptor.handleEventsWithWorkerPool(consumers);
        return new DisruptorQueue(_disruptor);
    }

    // 创建"发布订阅模式"的操作队列，即同一事件会被多个消费者并行消费
    public static <T> DisruptorQueue<T> getHandleEventsQueue(int queueSize, boolean isMoreProducer,
                                                             DisruptorConsumer<T>... consumers) {
        Disruptor<DisruptorEvent<T>> _disruptor = new Disruptor(new DisruptorEventFactory(),
                queueSize, Executors.defaultThreadFactory(),
                isMoreProducer ? ProducerType.MULTI : ProducerType.SINGLE,
                new SleepingWaitStrategy());
        _disruptor.handleEventsWith(consumers);
        return new DisruptorQueue(_disruptor);
    }

    // 直接通过传入的 Disruptor 对象创建操作队列（如果消费者有依赖关系的话可以用此方法）
    public static <T> DisruptorQueue<T> getQueue(Disruptor<DisruptorEvent<T>> disruptor) {
        return new DisruptorQueue(disruptor);
    }
}

