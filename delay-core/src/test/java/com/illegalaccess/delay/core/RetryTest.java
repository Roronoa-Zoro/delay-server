package com.illegalaccess.delay.core;

import com.illegalaccess.delay.message.SendResultEnum;
import com.illegalaccess.delay.toolkit.thread.AbstractLogRunnable;
import com.illegalaccess.delay.toolkit.thread.DelayThreadFactory;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RetryTest {

    @Test
    public void scheduleTest() throws InterruptedException {
        ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(2,
                new DelayThreadFactory("loop-delay-thread-"),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        pool.scheduleAtFixedRate(new ScheduleTask(), 3, 3, TimeUnit.SECONDS);

        TimeUnit.SECONDS.sleep(60);
    }

    class ScheduleTask extends AbstractLogRunnable {

        @Override
        public void runBusiness() {
            Random r = new Random();
            int i = r.nextInt();
            System.out.println(Thread.currentThread().getName() + "===" + i);
            if (i % 2 == 0) {
                throw new RuntimeException("test runtime");
            }
        }
    }

    @Test
    public void retry() {
        RetryConfig config = RetryConfig.<SendResultEnum>custom()
                .maxAttempts(2)
                .retryOnResult(sendResultEnum -> sendResultEnum == SendResultEnum.Failure)
                .retryExceptions(IOException.class, RuntimeException.class)
                .build();

        // Create a RetryRegistry with a custom global configuration
        RetryRegistry registry = RetryRegistry.of(config);

        // Get or create a Retry from the registry -
        // Retry will be backed by the default config
        Retry retryWithDefaultConfig = registry.retry("name1");


        for (int i = 0; i < 100; i++) {
            RetryTask tr = new RetryTask();
            retryWithDefaultConfig.executeSupplier(() -> tr.send());
        }
    }


    class RetryTask {
        Logger log = LoggerFactory.getLogger(RetryTask.class);

        public SendResultEnum send() {
            log.info("Thread=========={}", Thread.currentThread().getName() + ", at===" + new Date());
            return SendResultEnum.Failure;
        }
    }
}
