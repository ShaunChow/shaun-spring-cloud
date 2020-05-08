package com.shaun.gateway.gatewayzuul.fallback;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Component
public class GatewayFallbackProvider implements FallbackProvider {

    public String getRoute() {
        return "*";
    }

    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientHttpResponse() {

            public InputStream getBody() throws IOException {
                HashMap<String, Object> r = new HashMap<>();
                r.put("state", "9999");
                r.put("msg", "Service Is Broken...");
                return new ByteArrayInputStream(new ObjectMapper().writeValueAsString(r).getBytes(StandardCharsets.UTF_8));
            }

            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }

            public HttpStatus getStatusCode() {
                return HttpStatus.OK;
            }

            public int getRawStatusCode() {
                return HttpStatus.OK.value();
            }

            public String getStatusText() {
                return HttpStatus.OK.getReasonPhrase();
            }

            public void close() {

            }
        };
    }
}
