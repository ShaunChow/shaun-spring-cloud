package com.shaun.common.component;

import com.shaun.common.util.SystemClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.Map;

@ConditionalOnExpression("'${setting.shaun-rest-controller-advice.enable}'.equals('on')")
@RestControllerAdvice
public class ShaunRestControllerAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger log = LoggerFactory.getLogger(ShaunRestControllerAdvice.class);


    @ExceptionHandler({HttpClientErrorException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> HttpClientErrorException(HttpClientErrorException exception) {

        log.error(exception.toString(), exception);

        Map<String, Object> data = new HashMap<>();
        data.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        data.put("message", exception.getResponseBodyAsString());

        return data;
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> RuntimeException(RuntimeException exception) {

        log.error(exception.toString(), exception);

        Map<String, Object> data = new HashMap<>();
        data.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        data.put("message", exception.toString());

        return data;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> MethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @Override
    public boolean supports(
            MethodParameter methodParameter,
            Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object o,
            MethodParameter methodParameter,
            MediaType mediaType,
            Class<? extends HttpMessageConverter<?>> aClass,
            ServerHttpRequest serverHttpRequest,
            ServerHttpResponse serverHttpResponse) {

        Map<String, Object> result = new HashMap<>();
        result.put("code",200);
        result.put("data",o);
        result.put("msg","success");
        result.put("timestamp", SystemClock.millisClock().now());

        return result;
    }
}
