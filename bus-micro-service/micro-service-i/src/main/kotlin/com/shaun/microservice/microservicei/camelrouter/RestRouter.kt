package com.shaun.microservice.microservicei.camelrouter

import org.apache.camel.builder.RouteBuilder
import org.springframework.stereotype.Component

@Component
class RestRouter : RouteBuilder() {
    override fun configure() {
        from("timer://system?period={{timer.period}}").routeId("hello").routeGroup("hello-group")
                .transform().method("myBean", "saySomething")
                .filter(simple("\${body} contains 'foo'"))
                .to("log:foo")
                .end()
                .to("stream:out")
    }
}