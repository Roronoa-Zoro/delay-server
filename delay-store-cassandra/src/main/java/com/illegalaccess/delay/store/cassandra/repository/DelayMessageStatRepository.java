package com.illegalaccess.delay.store.cassandra.repository;

import com.illegalaccess.delay.store.cassandra.entity.DelayMessageStat;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DelayMessageStatRepository extends CassandraRepository<DelayMessageStat, String> {
}
