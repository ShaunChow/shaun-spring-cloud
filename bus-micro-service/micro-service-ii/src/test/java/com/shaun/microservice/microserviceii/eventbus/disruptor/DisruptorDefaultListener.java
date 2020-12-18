package com.shaun.microservice.microserviceii.eventbus.disruptor;

import com.shaun.microservice.microserviceii.application.config.disruptor.DisruptorConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DisruptorDefaultListener extends DisruptorConsumer<String> {

    private Logger logger = LoggerFactory.getLogger(DisruptorDefaultListener.class);

    @Override
    public void consume(String var1) {
        if (StringUtils.isEmpty(var1)) {
            logger.warn("receiver series data is empty!");
        }
        logger.info(var1);
    }

}
