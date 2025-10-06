package com.streamnz.practisee.service.handler.aop;

import com.streamnz.practisee.exceptions.OutageMaxRetryException;
import com.streamnz.practisee.model.dto.OutageEvent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author cheng hao
 * @Date 07/10/2025 00:43
 */
@Aspect
@Component
@Order(1)  // 先执行重试切面
@Slf4j
public class RetryAspect {

    @Value("${retry.maxRetries:3}")
    private int maxRetries;

    @Value("${retry.enabled:true}")
    private boolean retryEnabled;

    @Around("@annotation(OutageHandlerType)")
    public Object retry(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!retryEnabled) {
            return joinPoint.proceed();
        }

        OutageEvent event = (OutageEvent) joinPoint.getArgs()[0];
        int attempt = 0;
        
        while (attempt < maxRetries) {
            try {
                log.info("RetryAspect: Attempt {} to handle event {}", attempt + 1, event.getEventId());
                return joinPoint.proceed();
            } catch (Exception e) {
                attempt++;
                log.error("RetryAspect: Error handling event {} on attempt {}: {}", event.getEventId(), attempt, e.getMessage(), e);
                if (attempt >= maxRetries) {
                    log.error("RetryAspect: Max retries reached for event {}. Failing the operation.", event.getEventId());
                    throw new OutageMaxRetryException("Max retries reached for event", event.getEventId());
                }
            }
        }
        return null;
    }
}
