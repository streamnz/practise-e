package com.streamnz.practisee.service.handler.decorator;

import com.streamnz.practisee.exceptions.OutageMaxRetryException;
import com.streamnz.practisee.model.dto.OutageEvent;
import com.streamnz.practisee.service.handler.OutageHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author cheng hao
 * @Date 06/10/2025 20:30
 */
@Slf4j
public class RetryDecorator extends OutageHandlerDecorator{

    private int maxRetries;

    public RetryDecorator(OutageHandler wrappedHandler, int maxRetries) {
        super(wrappedHandler);
        if (maxRetries <= 0) {
            throw new OutageMaxRetryException("Invalid MaxRetries Configuration",null); //
        }
        this.maxRetries  = maxRetries;
    }

    @Override
    public void handle(OutageEvent event) {
        int attempt = 0;
        while (attempt < maxRetries) {
            try {
                log.info("RetryDecorator: Attempt {} to handle event {}", attempt + 1, event.getEventId());
                super.handle(event);

                return; // If successful, exit the method
            } catch (Exception e) {
                attempt++;
                log.error("RetryDecorator: Error handling event {} on attempt {}: {}", event.getEventId(), attempt, e.getMessage(), e);
                if (attempt >= maxRetries) {
                    log.error("RetryDecorator: Max retries reached for event {}. Failing the operation.", event.getEventId());
                    throw new OutageMaxRetryException("Max retries reached for event",event.getEventId()); // Rethrow the exception after max retries
                }
            }
        }
    }
}
