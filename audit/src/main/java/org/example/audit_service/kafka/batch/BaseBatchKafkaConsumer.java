package org.example.audit_service.kafka.batch;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class BaseBatchKafkaConsumer {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public abstract void listen(List<ConsumerRecord<String, String>> records);
}
