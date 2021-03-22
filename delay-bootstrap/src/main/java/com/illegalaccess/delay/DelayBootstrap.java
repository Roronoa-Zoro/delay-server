package com.illegalaccess.delay;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 * @author Jimmy Li
 * @date 2021-03-04 10:14
 */
@Slf4j
@EnableDubbo
@EnableScheduling
@SpringBootApplication
public class DelayBootstrap {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DelayBootstrap.class)
//                .web(WebApplicationType.NONE)
                .build()
                .run(args);
        log.info("DelayBootstrap is running");
    }
}
