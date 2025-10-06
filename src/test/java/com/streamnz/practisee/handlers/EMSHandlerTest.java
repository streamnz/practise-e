package com.streamnz.practisee.handlers;

import com.streamnz.practisee.enums.SourceSystemEnum;
import com.streamnz.practisee.model.dto.OutageEvent;
import com.streamnz.practisee.service.OutageService;
import com.streamnz.practisee.service.handler.EMSHandler;
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
 * @Date 07/10/2025 11:46
 */
@ExtendWith(MockitoExtension.class)
@DisplayName( "EMSHandler Test")
public class EMSHandlerTest {

    @InjectMocks
    private EMSHandler handler;

    @Mock
    private OutageService outageService;

    @Mock
    private OutageEventListenerRegister listenerRegister;

    private OutageEvent event;


    @BeforeEach
    void setUp() {
        event = new OutageEvent("3", SourceSystemEnum.EMS, Instant.now());
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
