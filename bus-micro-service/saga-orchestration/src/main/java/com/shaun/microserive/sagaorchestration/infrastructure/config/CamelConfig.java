package com.shaun.microserive.sagaorchestration.infrastructure.config;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.saga.InMemorySagaService;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfig {

    final CamelContext camelContext;

    public CamelConfig(CamelContext camelContext) throws Exception {
        this.camelContext = camelContext;

        this.camelContext.addService(new InMemorySagaService());
    }
}
