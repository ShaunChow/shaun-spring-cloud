package com.shaun.microservice.microservicei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("micro-service-ii")
public interface MicroServiceIIClient {
    @GetMapping("appinfo")
    Object getInfo();
}
