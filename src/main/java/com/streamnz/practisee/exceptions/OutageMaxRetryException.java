package com.streamnz.practisee.exceptions;

import lombok.ToString;

@ToString
public class OutageMaxRetryException extends RuntimeException {

    private String errorMessage = "Maximum retry attempts exceeded.";
    private String eventId = "N/A"; // You can set this to a specific

    public OutageMaxRetryException(String errorMessage, String eventId) {
        super("Maximum retry attempts exceeded.");
        this.errorMessage = errorMessage;
        this.eventId = eventId;
    }
}
