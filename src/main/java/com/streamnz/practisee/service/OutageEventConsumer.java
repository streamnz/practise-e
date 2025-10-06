package com.streamnz.practisee.service;

import com.streamnz.practisee.exceptions.OutageProcessingException;
import com.streamnz.practisee.model.dto.OutageEvent;
import com.streamnz.practisee.service.handler.HandlerRegister;
import com.streamnz.practisee.service.handler.OutageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author cheng hao
 * @Date 06/10/2025 16:28
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OutageEventConsumer {

    private final HandlerRegister handlerRegister;

    public void consume(OutageEvent event) {
        log.info("OutageEventConsumer: Received event - " + event);
        OutageHandler handler = handlerRegister.getHandler(event.getSourceSystem().getName());
        try {
            handler.handle(event);
        } catch (OutageProcessingException e) {
            log.error("OutageEventConsumer: Error processing event {}: {}", event.getEventId(), e.getMessage(), e);
            // Additional error handling logic can be added here
            // todo dead letter queue, alerting, etc.
            // todo record to database for further investigation
        }
    }
}
