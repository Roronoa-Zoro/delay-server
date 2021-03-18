package com.illegalaccess.delay.store.cassandra.repository;

import com.illegalaccess.delay.store.cassandra.entity.DelayMessage;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DelayMessageRepository extends CassandraRepository<DelayMessage, Long> {

}
