package com.illegalaccess.delay.store.cassandra.repository;

import com.illegalaccess.delay.store.cassandra.entity.DelayMessageApp;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DelayMessageAppRepository extends CassandraRepository<DelayMessageApp, String> {
}
