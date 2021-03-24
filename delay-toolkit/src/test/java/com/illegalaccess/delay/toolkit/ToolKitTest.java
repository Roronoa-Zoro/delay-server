package com.illegalaccess.delay.toolkit;

import com.google.common.base.Stopwatch;
import com.illegalaccess.delay.toolkit.http.HttpUtils;
import com.illegalaccess.delay.toolkit.json.JsonTool;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ToolKitTest {

    @Test
    public void httpTest() {
        String url = "http://127.0.0.1:8880/web/demo/post";
        String url2 = "http://127.0.0.1:8880/web/demo/postP";

        Stopwatch stopwatch = Stopwatch.createUnstarted();
        stopwatch.start();
        String resp1 = HttpUtils.httpPost(url, null, null);
        stopwatch.stop();
        System.out.println("resp1====" + resp1 + ", cost===" + stopwatch.toString());

        Map<String, String> data = new HashMap<>(2);
        data.put("name", "jimmy");
        data.put("dept", "超级公司");
        stopwatch.start();
        String resp2 = HttpUtils.httpPost(url2, data, null);
        stopwatch.stop();
        System.out.println("resp2====" + resp2 + ", cost===" + stopwatch.toString());
    }

    @Test
    public void timeTest() {
        LocalDateTime now = LocalDateTime.now();
        long n = System.currentTimeMillis();
        System.out.println(TimeUtils.toTimeStamp(now.plusMinutes(6)));
        System.out.println(n + 6 * 60 * 1000);
        // 1615205733767
        // 1615206152686

        long n2 = n + 36000;
        System.out.println(TimeUtils.toLocalDateTime(n));
        System.out.println(TimeUtils.toLocalDateTime(n2));

        long n3 = 1615202708035L;
        System.out.println(TimeUtils.toLocalDateTime(n3));

        System.out.println(TimeUtils.toLocalDateTime(1616380100527L));

    }

    @Test
    public void uuidTest() {
        System.out.println(UUID.randomUUID().toString().replace("-", ""));
        System.out.println(UUID.randomUUID().toString().replace("-", ""));
        String cnt = "12345";
        System.out.println(JsonTool.parseObject(cnt, Long.class));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime acc = now.minusMinutes(1);
        Duration duration = Duration.between(acc, now);
        System.out.println(duration.toMillis());

    }

    @Test
    public void scheduleTest() throws InterruptedException {
        ScheduledThreadPoolExecutor ss = new ScheduledThreadPoolExecutor(1);
        System.out.println("add s task==" + new Date());
        ss.scheduleAtFixedRate(() -> System.out.println(new Date()), 0, 5, TimeUnit.SECONDS);

        TimeUnit.SECONDS.sleep(10);
        System.out.println("will shutdown=======" + new Date());
        ss.shutdown();
    }

    @Test
    public void idPerfTest() throws InterruptedException {
        int loop = 50000;
        int pool = 8;
        ExecutorService es = Executors.newFixedThreadPool(pool);
        IdGenerator.longId();

        for (int i = 0; i < pool; i++) {
            es.submit(new IdPerf(loop));
        }

        TimeUnit.SECONDS.sleep(10);

    }

    class IdPerf implements Runnable {
        int loop;

        public IdPerf(int loop) {
            this.loop = loop;
        }

        @Override
        public void run() {
            long t = System.currentTimeMillis();
            while (loop > 0) {
//                IdGenerator.longId();
                IdGenerator.generate();
                loop--;
            }
            long t2 = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + " cost==" + (t2 - t));
        }
    }
}
