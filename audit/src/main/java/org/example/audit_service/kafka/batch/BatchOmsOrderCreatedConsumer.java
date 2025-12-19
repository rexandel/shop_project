package org.example.audit_service.kafka.batch;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.audit_service.DTO.AuditLogOrderRequest;
import org.example.audit_service.common.JsonUtil;
import org.example.audit_service.kafka.message.Message;
import org.example.audit_service.kafka.message.OrderCreatedMessage;
import org.example.audit_service.mappers.AuditLogMapper;
import org.example.audit_service.service.AuditLogService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BatchOmsOrderCreatedConsumer extends BaseBatchKafkaConsumer {

    private final AuditLogService auditLogService;
    private final AuditLogMapper auditLogMapper;

    @Override
    @KafkaListener(topics = "#{@kafkaSettings.omsOrderCreatedTopic}", groupId = "#{@kafkaSettings.groupId}", concurrency = "3")
    public void listen(List<ConsumerRecord<String, String>> records) {
        log.info("Batch processing {} OrderCreated records", records.size());
        List<AuditLogOrderRequest.LogOrder> allLogOrders = new ArrayList<>();

        for (ConsumerRecord<String, String> record : records) {
            try {
                String json = record.value();
                String key = record.key();

                Message<OrderCreatedMessage> message = new Message<>(key, JsonUtil.fromJson(json, OrderCreatedMessage.class));
                OrderCreatedMessage order = message.getBody();

                if (order.getOrderItems() != null) {
                    order.getOrderItems().forEach(item -> {
                        allLogOrders.add(auditLogMapper.toLogOrder(order, item));
                    });
                }
            } catch (Exception e) {
                log.error("Failed to parse OrderCreatedMessage: {}", record.value(), e);
            }
        }

        if (!allLogOrders.isEmpty()) {
            auditLogService.logOrders(allLogOrders);
        }
    }
}
