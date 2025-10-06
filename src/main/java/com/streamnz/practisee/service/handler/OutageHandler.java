package com.streamnz.practisee.service.handler;

import com.streamnz.practisee.exceptions.OutageProcessingException;
import com.streamnz.practisee.model.dto.OutageEvent;

/**
 * @Author cheng hao
 * @Date 06/10/2025 19:48
 */
public interface OutageHandler {
    /**
     * Handle the outage event
     * @param event
     */
    void handle(OutageEvent event) throws OutageProcessingException;
}
