package com.shaun.microservice.microservicei.fallback;


import com.shaun.microservice.microservicei.feign.MicroServiceIIClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class MicroServiceIIFallBack implements MicroServiceIIClient {

    @Override
    public Object getInfo() {

        HashMap<String, Object> result = new HashMap<>();

        result.put("method", "feign");
        result.put("name", "resilence...");
        result.put("service_id", "micro-service-ii");


        return result;
    }
}

