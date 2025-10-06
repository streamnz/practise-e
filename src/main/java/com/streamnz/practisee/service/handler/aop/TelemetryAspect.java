package com.streamnz.practisee.service.handler.aop;

import com.streamnz.practisee.exceptions.OutageTelemetryHandleException;
import com.streamnz.practisee.model.dto.OutageEvent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * @Author cheng hao
 * @Date 07/10/2025 00:44
 */
@Aspect
@Component
@Order(2)  // 在重试切面之后执行
@Slf4j
public class TelemetryAspect {

    @Around("@annotation(OutageHandlerType)")
    public Object handleWithTelemetry(ProceedingJoinPoint joinPoint) throws Throwable {
        OutageEvent event = (OutageEvent) joinPoint.getArgs()[0];
        Instant start = Instant.now();
        String handlerName = joinPoint.getTarget().getClass().getSimpleName();
        String eventId = event.getEventId();
        
        try {
            log.info("TelemetryAspect: Start handling event {} with handler {} at {}", eventId, handlerName, start);
            
            Object result = joinPoint.proceed();
            
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            log.info("TelemetryAspect: Finished handling event {} with handler {} at {}. Duration: {} ms", eventId, handlerName, end, duration.toMillis());
            
            return result;
        } catch (Exception e) {
            log.error("TelemetryAspect: Error handling event {}: {}", eventId, e.getMessage(), e);
            Duration duration = Duration.between(start, Instant.now());
            log.info("TelemetryAspect: Handling event {} failed after {} ms", eventId, duration.toMillis());
            throw new OutageTelemetryHandleException("TelemetryAspect: Error handling event " + eventId, eventId);
        }
    }
}
