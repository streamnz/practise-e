package com.streamnz.practisee.service.handler.decorator;

import com.streamnz.practisee.exceptions.OutageTelemetryHandleException;
import com.streamnz.practisee.model.dto.OutageEvent;
import com.streamnz.practisee.service.OutageService;
import com.streamnz.practisee.service.TelemetryService;
import com.streamnz.practisee.service.handler.OutageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * @Author cheng hao
 * @Date 06/10/2025 20:09
 */
@Component
@Slf4j
public class TelemetryDecorator extends OutageHandlerDecorator{

    private final TelemetryService telemetryService;

    public TelemetryDecorator(OutageHandler deligate, TelemetryService telemetryService) {
        super(deligate);
        this.telemetryService = telemetryService;
    }

    @Override
    public void handle(OutageEvent event) {
            Instant start = Instant.now();
            String handlerName = deligate.getClass().getSimpleName();
            String eventId = event.getEventId();
        try{
            log.info("TelemetryDecorator: Start handling event {} with handler {} at {}", eventId, handlerName, start);
            super.handle(event);
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            log.info("TelemetryDecorator: Finished handling event {} with handler {} at {}. Duration: {} ms",
                    eventId, handlerName, end, duration.toMillis());

            // record success metrics here if needed todo
        } catch (Exception e) {
            log.error("TelemetryDecorator: Error handling event {}: {}", event.getEventId(), e.getMessage(), e);
            Duration duration = Duration.between(Instant.now(), Instant.now());
            log.info("TelemetryDecorator: Handling event {} failed after {} ms", eventId, duration.toMillis());
            // record failure metrics here if needed todo
            throw new OutageTelemetryHandleException("TelemetryDecorator: Error handling event " + eventId, eventId);
        }

    }

}
