package com.streamnz.practisee.service.handler.listeners;

import com.streamnz.practisee.model.dto.OutageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author cheng hao
 * @Date 06/10/2025 23:26
 */
@Component
@Slf4j
public class UrbanCriticalListener implements OutageEventListener{

    @Override
    public void onOutageEvent(OutageEvent event) {
        log.info("UrbanCriticalListener: Received event - " + event);
    }
}
