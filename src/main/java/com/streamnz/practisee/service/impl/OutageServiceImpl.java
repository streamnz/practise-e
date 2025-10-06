package com.streamnz.practisee.service.impl;

import com.streamnz.practisee.model.dto.OutageEvent;
import com.streamnz.practisee.service.OutageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author cheng hao
 * @Date 06/10/2025 19:39
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OutageServiceImpl implements OutageService {

    // dao layer injection here

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveEvent(OutageEvent event) {
        log.info("OutageServiceImpl: Saving event to database - " + event);
        // Implement the actual save logic here, e.g., using a repository to persist the event
    }
}
