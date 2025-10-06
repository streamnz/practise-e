package com.streamnz.practisee.service.handler;

import com.streamnz.practisee.model.dto.OutageEvent;
import com.streamnz.practisee.service.OutageService;
import com.sun.nio.sctp.HandlerResult;


/**
 * @Author cheng hao
 * @Date 06/10/2025 19:28
 */
public abstract class OutageHandleTemplate implements OutageHandler {

    private final OutageService outageService;

    protected OutageHandleTemplate(OutageService outageService) {
        this.outageService = outageService;
    }

    @Override
    public final void handle(OutageEvent event) {
        checkValidation(event);
        normalize(event);
        calculatePriority(event);
        notifyStakeholders(event);
        saveToDatabase(event);
    }

    protected abstract void checkValidation(OutageEvent event);
    protected abstract void normalize(OutageEvent event);
    protected abstract void calculatePriority(OutageEvent event);
    protected abstract void notifyStakeholders(OutageEvent event);


    protected void saveToDatabase(OutageEvent event) {
        System.out.println("Saving event to database: " + event);
        outageService.saveEvent(event);
    }
}
