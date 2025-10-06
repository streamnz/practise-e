package com.streamnz.practisee.service.handler.listeners;

import com.streamnz.practisee.model.dto.OutageEvent;

/**
 * @Author cheng hao
 * @Date 06/10/2025 23:11
 */
public interface OutageEventListener {

    void onOutageEvent(OutageEvent event);
}
