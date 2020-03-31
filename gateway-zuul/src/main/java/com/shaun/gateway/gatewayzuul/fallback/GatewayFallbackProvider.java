package com.shaun.gateway.gatewayzuul.fallback;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class GatewayFallbackProvider implements FallbackProvider {

    public String getRoute() {
        return "*";
    }

    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientHttpResponse() {

            public InputStream getBody() throws IOException {
                Map r = new HashMap<String, Object>();
                r.put("state", "9999");
                r.put("msg", "Service Is Broken...");
                return new ByteArrayInputStream(new ObjectMapper().writeValueAsString(r).getBytes("UTF-8"));
            }

            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                return headers;
            }

            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            public int getRawStatusCode() throws IOException {
                return HttpStatus.OK.value();
            }

            public String getStatusText() throws IOException {
                return HttpStatus.OK.getReasonPhrase();
            }

            public void close() {

            }
        };
    }
}
