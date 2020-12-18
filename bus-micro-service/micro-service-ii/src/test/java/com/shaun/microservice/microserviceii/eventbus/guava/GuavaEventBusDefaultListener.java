package com.shaun.microservice.microserviceii.eventbus.guava;

import com.google.common.eventbus.Subscribe;
import com.shaun.microservice.microserviceii.application.config.eventbus.LocalEventListening;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class GuavaEventBusDefaultListener implements LocalEventListening {

    private Logger logger = LoggerFactory.getLogger(GuavaEventBusDefaultListener.class);

    @Subscribe
    public void receive(String var1) {
        if (StringUtils.isEmpty(var1)) {
            logger.warn("receiver series data is empty!");
        }
        logger.info(var1);
    }

}