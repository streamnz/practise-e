package com.streamnz.practisee.exceptions;

public class OutageTelemetryHandleException extends RuntimeException {

    private String eventId = "N/A"; // You can set this to a specific event ID if available

    public OutageTelemetryHandleException(String message, String eventId) {
      super(message);
      this.eventId = eventId;
    }
}
