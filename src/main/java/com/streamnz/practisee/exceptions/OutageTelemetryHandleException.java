package com.streamnz.practisee.exceptions;

import lombok.ToString;

@ToString
public class OutageTelemetryHandleException extends RuntimeException {

    private String eventId = "N/A"; // You can set this to a specific event ID if available

    public OutageTelemetryHandleException(String message, String eventId) {
      super(message);
      this.eventId = eventId;
    }
}
