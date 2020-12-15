package com.shaun.microservice.microserviceii.application.config.eventbus;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalDeadEventListener {

    private static final Logger log = LoggerFactory.getLogger(LocalDeadEventListener.class);

    @Subscribe
    public void handle(DeadEvent e) {
        log.error("LocalDeadEvent source-> " + e.getSource() + "  event-> " + e.getEvent());
    }
}
