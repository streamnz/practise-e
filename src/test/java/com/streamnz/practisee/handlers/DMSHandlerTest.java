package com.streamnz.practisee.handlers;

import com.streamnz.practisee.enums.SourceSystemEnum;
import com.streamnz.practisee.model.dto.OutageEvent;
import com.streamnz.practisee.service.OutageService;
import com.streamnz.practisee.service.handler.DMSHandler;
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
 * @Date 07/10/2025 11:41
 */
@ExtendWith(MockitoExtension.class)
@DisplayName( "DMSHandler Test")
public class DMSHandlerTest {

    @InjectMocks
    private DMSHandler handler;

    @Mock
    private OutageService outageService;

    @Mock
    private OutageEventListenerRegister listenerRegister;

    private OutageEvent event;

    @BeforeEach
    void setUp() {
        event = new OutageEvent("2", SourceSystemEnum.DMS, Instant.now());
    }

    @Test
    @DisplayName("Should handle event successfully")
    void shouldHandleEventSuccessfully() {
        // when
        handler.handle(event);
        // then
        // 验证 outageService 的 saveEvent 方法被调用，确保事件被处理
        verify(outageService).saveEvent(event);
    }
}
