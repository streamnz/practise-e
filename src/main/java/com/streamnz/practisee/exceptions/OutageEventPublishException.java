package com.streamnz.practisee.exceptions;

import lombok.ToString;

@ToString
public class OutageEventPublishException extends RuntimeException {
    private String eventId = "N/A";

    public OutageEventPublishException(String message, String eventId) {
        super(message);
        this.eventId = eventId;
    }
}
