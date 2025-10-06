package com.streamnz.practisee.service.handler;

import com.streamnz.practisee.model.dto.OutageEvent;
import com.streamnz.practisee.service.OutageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author cheng hao
 * @Date 06/10/2025 19:45
 */
@Component
@Slf4j
@OutageHandlerType("DMS")
public class DMSHandler extends OutageHandleTemplate{

    protected DMSHandler(OutageService outageService) {
        super(outageService);
    }

    @Override
    protected void checkValidation(OutageEvent event) {

    }

    @Override
    protected void normalize(OutageEvent event) {

    }

    @Override
    protected void calculatePriority(OutageEvent event) {

    }

    @Override
    protected void notifyStakeholders(OutageEvent event) {

    }
}
