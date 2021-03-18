//package com.illegalaccess.delay;
//
//import com.illegalaccess.delay.store.StoreApi;
//import com.illegalaccess.delay.store.cassandra.entity.DelayMessage;
//import com.illegalaccess.delay.store.cassandra.repository.DelayMessageRepository;
//import com.illegalaccess.delay.store.cassandra.service.CassandraStoreService;
//import com.illegalaccess.delay.toolkit.IdGenerator;
//import com.illegalaccess.delay.toolkit.TimeUtils;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.cassandra.core.CassandraOperations;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.util.concurrent.ListenableFuture;
//
//import java.time.LocalDateTime;
//import java.util.concurrent.ExecutionException;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class DelayTest {
//
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//    @Autowired
//    private CassandraStoreService cassandraStoreService;
//    @Autowired
//    private CassandraOperations cassandraOperations;
//    @Autowired
//    DelayMessageRepository delayMessageRepository;
//
//    @Test
//    public void kafkaTest() throws ExecutionException, InterruptedException {
//        ListenableFuture<SendResult<String, String>> lis =  kafkaTemplate.send("delay-topic", "test-topic-msg");
//        SendResult<String, String> sr = lis.get();
//        System.out.println(sr.getProducerRecord());
//    }
//
//    @Test
//    public void cassandraTest() {
//        DelayMessage delayMessage = new DelayMessage();
////        delayMessage.setId(System.currentTimeMillis());
//        delayMessage.setSlot(1);
//        delayMessage.setAppKey("demoApp");
//        delayMessage.setStatus(1);
//        delayMessage.setMsgContent("sssss");
//        delayMessage.setMsgId(IdGenerator.generate());
//        delayMessage.setModifyTime(LocalDateTime.now());
//        delayMessage.setExecTime(LocalDateTime.now());
//        delayMessage.setModifyTime(LocalDateTime.now());
//        delayMessage.setTopic("delay-topic");
//        cassandraOperations.insert(delayMessage);
////        delayMessageRepository.save(delayMessage);
//    }
//}
