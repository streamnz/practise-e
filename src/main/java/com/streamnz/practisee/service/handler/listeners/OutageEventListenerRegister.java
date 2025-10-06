package com.streamnz.practisee.service.handler.listeners;

import com.streamnz.practisee.exceptions.OutageEventPublishException;
import com.streamnz.practisee.model.dto.OutageEvent;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author cheng hao
 * @Date 06/10/2025 23:12
 */
@Component
@Slf4j
public class OutageEventListenerRegister implements ApplicationContextAware {

    private final List<OutageEventListener> listeners;
    private ApplicationContext applicationContext;

    public OutageEventListenerRegister() {
        this.listeners = new CopyOnWriteArrayList<>();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        Map<String, OutageEventListener> beansOfType = applicationContext.getBeansOfType(OutageEventListener.class);
        listeners.addAll(beansOfType.values());
        log.info("Registered {} OutageEventListeners", listeners.size());
    }

    public void publishEvent(OutageEvent event) {
        for (OutageEventListener listener : listeners) {
            try {
                listener.onOutageEvent(event);
            } catch (Exception e) {
                log.error("Error while notifying listener: {}", listener.getClass().getName(), e);
                throw new OutageEventPublishException("Error while notifying listener", event.getEventId());
            }
        }
    }
}
