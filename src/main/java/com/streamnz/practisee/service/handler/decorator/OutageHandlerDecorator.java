package com.streamnz.practisee.service.handler.decorator;

import com.streamnz.practisee.exceptions.OutageProcessingException;
import com.streamnz.practisee.model.dto.OutageEvent;
import com.streamnz.practisee.service.handler.OutageHandler;

/**
 * @Author cheng hao
 * @Date 06/10/2025 20:08
 */
public abstract class OutageHandlerDecorator implements OutageHandler {

    protected final OutageHandler deligate;

    protected OutageHandlerDecorator(OutageHandler wrappedHandler) {
        this.deligate = wrappedHandler;
    }

    @Override
    public void handle(OutageEvent event) throws OutageProcessingException {
        deligate.handle(event);
    }
}
