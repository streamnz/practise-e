package com.streamnz.practisee.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author cheng hao
 * @Date 06/10/2025 20:31
 */
@Configuration
@Slf4j
@Getter
public class DecoratorConfig {

    @Value("${retry.maxRetries:3}")
    private int maxRetries;

    @Value("${retry.enabled:true}")
    private boolean retryEnabled;


}
