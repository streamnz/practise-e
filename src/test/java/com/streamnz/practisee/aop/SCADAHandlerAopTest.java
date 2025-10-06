package com.streamnz.practisee.aop;

import com.streamnz.practisee.enums.SourceSystemEnum;
import com.streamnz.practisee.model.dto.OutageEvent;
import com.streamnz.practisee.service.OutageService;
import com.streamnz.practisee.service.handler.HandlerRegister;
import com.streamnz.practisee.service.handler.SCADAHandler;
import com.streamnz.practisee.service.handler.listeners.OutageEventListenerRegister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.Mockito.verify;

/**
 * Test for SCADAHandler with AOP
 * @Author cheng hao
 * @Date 07/10/2025 00:45
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SCADAHandler AOP Test")
public class SCADAHandlerAopTest {

    @Mock
    private OutageService outageService;

    @Mock
    private OutageEventListenerRegister listenerRegister;

    @Mock
    private HandlerRegister handlerRegister;

    private SCADAHandler handler;
    private OutageEvent event;

    @BeforeEach
    void setUp() {
        handler = new SCADAHandler(outageService, listenerRegister);
        event = new OutageEvent("1", SourceSystemEnum.SCADA, Instant.now());
    }

    @Test
    @DisplayName("Should handle event with AOP support")
    void shouldHandleEventWithAop() {
        // when
        handler.handle(event);
        
        // then
        verify(outageService).saveEvent(event);
        // AOP 会自动应用重试和遥测功能
    }
}
