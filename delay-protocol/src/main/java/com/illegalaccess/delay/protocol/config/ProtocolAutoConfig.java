package com.illegalaccess.delay.protocol.config;

import com.illegalaccess.delay.protocol.event.ResourceChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ProtocolAutoConfig {

    @Bean
    public ResourceChangeListener resourceChangeListener() {
        return new ResourceChangeListener();
    }
}
