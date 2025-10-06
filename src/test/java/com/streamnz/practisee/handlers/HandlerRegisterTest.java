package com.streamnz.practisee.handlers;

import com.streamnz.practisee.service.OutageService;
import com.streamnz.practisee.service.handler.HandlerRegister;
import com.streamnz.practisee.service.handler.OutageHandler;
import com.streamnz.practisee.service.handler.SCADAHandler;
import com.streamnz.practisee.service.handler.aop.OutageHandlerType;
import com.streamnz.practisee.service.handler.listeners.OutageEventListenerRegister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

/**
 * @Author cheng hao
 * @Date 07/10/2025 12:07
 */
@ExtendWith(MockitoExtension.class)
@DisplayName( "HandlerRegister Test")
public class HandlerRegisterTest {

    @Mock private ApplicationContext context;
    @Mock private OutageService outageService;
    @Mock private OutageEventListenerRegister listenerRegister;
    private HandlerRegister handlerRegister;

    @BeforeEach
    void setup() {
        handlerRegister = new HandlerRegister();
        handlerRegister.setApplicationContext(context);
    }

    @Test
    @DisplayName("Should register and retrieve handler successfully")
    void registerAndRetrieveHandler() {
        // given
        SCADAHandler handler = new SCADAHandler(outageService,listenerRegister);
        when(context.getBeansWithAnnotation(OutageHandlerType.class)).thenReturn(Map.of("SCADA", handler));
        // when
        handlerRegister.init();
        // then
        OutageHandler registerHandler = handlerRegister.getHandler("SCADA");
        assertThat(registerHandler).isSameAs(handler);

    }

    @Test
    @DisplayName("Should throw exception when no handler found")
    void testGetHandler_NotFound() {
        // given
        when(context.getBeansWithAnnotation(OutageHandlerType.class)).thenReturn(Map.of());
        handlerRegister.init();
        // when & then
        try {
            handlerRegister.getHandler("NON_EXISTENT");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("No handler found for type: NON_EXISTENT");
        }
    }



}
