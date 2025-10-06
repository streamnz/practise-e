package com.streamnz.practisee.handlers;

import com.streamnz.practisee.enums.SourceSystemEnum;
import com.streamnz.practisee.model.dto.OutageEvent;
import com.streamnz.practisee.service.OutageService;
import com.streamnz.practisee.service.handler.SCADAHandler;
import com.streamnz.practisee.service.handler.listeners.OutageEventListenerRegister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.Mockito.verify;

/**
 * @Author cheng hao
 * @Date 06/10/2025 21:21
 */
@ExtendWith(MockitoExtension.class)
@DisplayName( "SCADAHandler Test")
public class SCADAHandlerTest {

    @Mock
    private OutageService outageService;

    @Mock
    private OutageEventListenerRegister listenerRegister;

    @InjectMocks
    private SCADAHandler handler;

    private OutageEvent event;

    @BeforeEach
    void setUp() {
        event = new OutageEvent("1", SourceSystemEnum.SCADA, Instant.now());
    }

    @Test
    @DisplayName("Should handle event successfully")
    void shouldHandleEventSuccessfully() {
        // when
        handler.handle(event);
        // then
        verify(outageService).saveEvent(event);
    }



}
