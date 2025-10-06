package com.streamnz.practisee.service.handler;

import com.streamnz.practisee.model.dto.OutageEvent;
import com.streamnz.practisee.service.OutageService;
import com.streamnz.practisee.service.handler.listeners.OutageEventListenerRegister;


/**
 * @Author cheng hao
 * @Date 06/10/2025 19:28
 */
public abstract class OutageHandleTemplate implements OutageHandler {

    private final OutageService outageService;

    private final OutageEventListenerRegister listenerRegister;

    protected OutageHandleTemplate(OutageService outageService, OutageEventListenerRegister listenerRegister) {
        this.outageService = outageService;
        this.listenerRegister = listenerRegister;
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

    /**
     * Notify all registered listeners about the outage event
     *
     * @param event
     */
    protected void notifyStakeholders(OutageEvent event) {
        listenerRegister.publishEvent(event);
    }


    /**
     * Persist the outage event to the database
     * @param event
     */
    protected void saveToDatabase(OutageEvent event) {
        System.out.println("Saving event to database: " + event);
        outageService.saveEvent(event);
    }
}
