package com.streamnz.practisee.exceptions;

import lombok.ToString;

/**
 * @Author cheng hao
 * @Date 06/10/2025 21:45
 */
@ToString
public class OutageProcessingException extends Exception {

    private final String eventId;

    public OutageProcessingException(String message, String eventId) {
        super(message);
        this.eventId = eventId;
    }

    public OutageProcessingException(String message, Throwable cause, String eventId) {
        super(message, cause);
        this.eventId = eventId;
    }
}
