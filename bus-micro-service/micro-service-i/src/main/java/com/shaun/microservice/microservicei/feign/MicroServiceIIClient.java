package com.shaun.microservice.microservicei.feign;

import com.shaun.microservice.microservicei.fallback.MicroServiceIIFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "micro-service-ii", fallback = MicroServiceIIFallBack.class)
public interface MicroServiceIIClient {
    @GetMapping("appinfo")
    Object getInfo();
}
