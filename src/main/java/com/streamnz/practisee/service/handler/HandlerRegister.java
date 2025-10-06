package com.streamnz.practisee.service.handler;

import com.streamnz.practisee.service.handler.decorator.DecoratorFactory;
import com.streamnz.practisee.service.handler.decorator.RetryDecorator;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author cheng hao
 * @Date 06/10/2025 19:46
 */
@Component
@Slf4j
public class HandlerRegister implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private DecoratorFactory decoratorFactory;
    // Map to hold handlers by type
    private final Map<String, OutageHandler> handlerMap;

    public HandlerRegister(DecoratorFactory decoratorFactory) {
        this.handlerMap = new ConcurrentHashMap<>();
        this.decoratorFactory = decoratorFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(OutageHandlerType.class);
        beansWithAnnotation.forEach((name, bean) -> {
            if (bean instanceof OutageHandler handler) {
                OutageHandlerType annotation = bean.getClass().getAnnotation(OutageHandlerType.class);
                if (annotation != null) {
                    String type = annotation.value();
                    // Wrap the handler with decorators
                    OutageHandler decorator = decoratorFactory.createDecorator(handler);
                    handlerMap.put(type, decorator);
                    log.info("Registered handler for type: {}", type);
                }
            }
        });
    }

    public OutageHandler getHandler(String type) {
        OutageHandler outageHandler = handlerMap.get(type);
        if (outageHandler == null) {
            throw new IllegalArgumentException("No handler found for type: " + type);
        }
        return outageHandler;
    }
}
