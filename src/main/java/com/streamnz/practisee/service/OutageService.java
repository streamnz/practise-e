package com.streamnz.practisee.service;

import com.streamnz.practisee.model.dto.OutageEvent;

/**
 * @Author cheng hao
 * @Date 06/10/2025 19:39
 */
public interface OutageService {
    /**
     * Save the outage event to the database.
     * @param event
     */
    void saveEvent(OutageEvent event);
}
