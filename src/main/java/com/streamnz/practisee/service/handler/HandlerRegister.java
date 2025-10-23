package com.streamnz.practisee.service.handler;

import com.streamnz.practisee.service.handler.aop.OutageHandlerType;
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
    // Map to hold handlers by type
    private final Map<String, OutageHandler> handlerMap;

    public HandlerRegister() {
        this.handlerMap = new ConcurrentHashMap<>();
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
                    // 直接注册处理器，AOP 会自动应用
                    handlerMap.put(type, handler);
                    log.info("Registered handler for type: {} with AOP support", type);
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

