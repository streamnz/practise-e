package com.streamnz.practisee.service.handler.decorator;

import com.streamnz.practisee.config.DecoratorConfig;
import com.streamnz.practisee.exceptions.OutageMaxRetryException;
import com.streamnz.practisee.exceptions.OutageProcessingException;
import com.streamnz.practisee.model.dto.OutageEvent;
import com.streamnz.practisee.service.handler.OutageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author cheng hao
 * @Date 06/10/2025 20:30
 */
@Component
@Slf4j
public class RetryDecorator extends OutageHandlerDecorator{

    private DecoratorConfig decoratorConfig;

    public RetryDecorator(OutageHandler wrappedHandler, DecoratorConfig decoratorConfig) {
        super(wrappedHandler);
        this.decoratorConfig = decoratorConfig;
    }

    @Override
    public void handle(OutageEvent event) {
        int maxRetries = decoratorConfig.getMaxRetries();
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
