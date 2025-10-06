package com.streamnz.practisee.service.handler.decorator;

import com.streamnz.practisee.config.DecoratorConfig;
import com.streamnz.practisee.service.handler.OutageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author cheng hao
 * @Date 06/10/2025 23:50
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DecoratorFactory {

    private final DecoratorConfig config;

    public OutageHandler createDecorator(OutageHandler handler) {
        OutageHandler decoratedHandler = handler;
        if (config.isRetryEnabled()) {
            log.info("Applying RetryDecorator with maxRetries={}", config.getMaxRetries());
            decoratedHandler = new TelemetryDecorator(new RetryDecorator(decoratedHandler, config.getMaxRetries()));
        }
        // Future decorators can be added here
        return decoratedHandler;
    }
}
