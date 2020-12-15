package com.shaun.microservice.microserviceii.application.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

@Order(1)
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private final Environment env;

    private ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    private ObjectMapper objectMapper = new ObjectMapper();

    public LoggingAspect(Environment env) {
        this.env = env;
    }

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut("within(com.shaun.microservice.microserviceii.application.service..*)" +
            " || within(com.shaun.microservice.microserviceii.domain.service..*)" +
            " || within(com.shaun.microservice.microserviceii.infrastructure.persistence..*)" +
            " || within(com.shaun.microservice.microserviceii.xfacade.rest..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @Pointcut("execution(public * com.shaun.microservice.microserviceii.xfacade.rest.*.*(..) )")
    public void restControllerPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @Before("restControllerPointcut()")
    public void deBefore(JoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes attributes
                = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        log.info("URL : " + request.getRequestURL().toString() + "?" + request.getQueryString());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());
        log.info("CLASS_METHOD : " + joinPoint.getTarget().toString() + "." + joinPoint.getSignature().getName());
        log.info("ARGS : " + objectMapper.writeValueAsString(joinPoint.getArgs()));
        log.info("HEADER_TOKEN : " + request.getHeader("Token"));
    }

    @AfterReturning(returning = "ret", pointcut = "restControllerPointcut()")
    public void doAfterReturning(JoinPoint jp, Object ret) throws Throwable {

        if (startTime.get() != null) {
            log.info("Spend_Time : " + (LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli() - startTime.get()) + "ms");
            log.info("============= " + jp.getTarget() + " End =========================");
        }
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice.
     * @param e exception.
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        if (env.acceptsProfiles(Profiles.of("dev"))) {
            log.error("Exception in {}.{}() with cause = \'{}\' and exception = \'{}\'", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), e.getCause() != null? e.getCause() : "NULL", e.getMessage(), e);

        } else {
            log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), e.getCause() != null? e.getCause() : "NULL");
        }
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice.
     * @return result.
     * @throws Throwable throws {@link IllegalArgumentException}.
     */
    @Around("applicationPackagePointcut() && springBeanPointcut() && restControllerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("============= " + joinPoint.getTarget() + " Start =========================");
        startTime.set(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());

        log.info("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));

        try {
            Object result = joinPoint.proceed();

            log.info("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), result);

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

            throw e;
        }
    }
}
