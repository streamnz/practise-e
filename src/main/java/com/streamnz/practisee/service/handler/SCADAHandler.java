package com.streamnz.practisee.service.handler;

import com.streamnz.practisee.model.dto.OutageEvent;
import com.streamnz.practisee.service.OutageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author cheng hao
 * @Date 06/10/2025 19:36
 */
@Service
@Slf4j
@OutageHandlerType("SCADA")
public class SCADAHandler extends OutageHandleTemplate{


    protected SCADAHandler(OutageService outageService) {
        super(outageService);
    }

    @Override
    protected void checkValidation(OutageEvent event) {
        log.info("SCADAHandler: Validating event - " + event);
        // Add SCADA-specific validation logic here
    }

    @Override
    protected void normalize(OutageEvent event) {
        log.info("SCADAHandler: Normalizing event - " + event);
    }

    @Override
    protected void calculatePriority(OutageEvent event) {
        log.info("SCADAHandler: Calculating priority for event - " + event);
    }

    @Override
    protected void notifyStakeholders(OutageEvent event) {
        log.info("SCADAHandler: Notifying stakeholders for event - " + event);
    }
}
