package org.example.audit_service.kafka.batch;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.audit_service.DTO.AuditLogOrderRequest;
import org.example.audit_service.common.JsonUtil;
import org.example.audit_service.kafka.message.Message;
import org.example.audit_service.kafka.message.OrderStatusChangedMessage;
import org.example.audit_service.mappers.AuditLogMapper;
import org.example.audit_service.service.AuditLogService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BatchOmsOrderStatusChangedConsumer extends BaseBatchKafkaConsumer {

    private final AuditLogService auditLogService;
    private final AuditLogMapper auditLogMapper;

    @Override
    @KafkaListener(topics = "#{@kafkaSettings.omsOrderStatusChangedTopic}", groupId = "#{@kafkaSettings.groupId}", concurrency = "3")
    public void listen(List<ConsumerRecord<String, String>> records) {
        log.info("Batch processing {} OrderStatusChanged records", records.size());
        List<AuditLogOrderRequest.LogOrder> allLogOrders = new ArrayList<>();

        for (ConsumerRecord<String, String> record : records) {
            try {
                String json = record.value();
                String key = record.key();

                Message<OrderStatusChangedMessage> message = new Message<>(key, JsonUtil.fromJson(json, OrderStatusChangedMessage.class));
                OrderStatusChangedMessage msg = message.getBody();

                allLogOrders.add(auditLogMapper.toLogOrder(msg));
            } catch (Exception e) {
                log.error("Failed to parse OrderStatusChangedMessage: {}", record.value(), e);
            }
        }

        if (!allLogOrders.isEmpty()) {
            auditLogService.logOrders(allLogOrders);
        }
    }
}
