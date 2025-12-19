package org.example.audit_service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.audit_service.DTO.AuditLogOrderRequest;
import org.example.audit_service.common.JsonUtil;
import org.example.audit_service.kafka.message.Message;
import org.example.audit_service.kafka.message.OrderCreatedMessage;
import org.example.audit_service.kafka.message.OrderStatusChangedMessage;
import org.example.audit_service.mappers.AuditLogMapper;
import org.example.audit_service.service.AuditLogService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final AuditLogService auditLogService;
    private final AuditLogMapper auditLogMapper;

    // @KafkaListener(topics = "#{@kafkaSettings.omsOrderCreatedTopic}", groupId = "#{@kafkaSettings.groupId}", concurrency = "3")
    public void listenOrderCreated(ConsumerRecord<String, String> record) {
        log.info("Received OrderCreated message: key={}", record.key());
        try {
            String json = record.value();
            String key = record.key();
            
            Message<OrderCreatedMessage> message = new Message<>(key, JsonUtil.fromJson(json, OrderCreatedMessage.class));
            OrderCreatedMessage order = message.getBody();

            if (order.getOrderItems() != null) {
                List<AuditLogOrderRequest.LogOrder> logOrders = new ArrayList<>();
                order.getOrderItems().forEach(item -> {
                    logOrders.add(auditLogMapper.toLogOrder(order, item));
                });
                if (!logOrders.isEmpty()) {
                    auditLogService.logOrders(logOrders);
                }
            }
        } catch (Exception e) {
            log.error("Failed to parse OrderCreatedMessage: {}", record.value(), e);
        }
    }

    // @KafkaListener(topics = "#{@kafkaSettings.omsOrderStatusChangedTopic}", groupId = "#{@kafkaSettings.groupId}", concurrency = "3")
    public void listenOrderStatusChanged(ConsumerRecord<String, String> record) {
        log.info("Received OrderStatusChanged message: key={}", record.key());
        try {
            String json = record.value();
            String key = record.key();

            Message<OrderStatusChangedMessage> message = new Message<>(key, JsonUtil.fromJson(json, OrderStatusChangedMessage.class));
            OrderStatusChangedMessage msg = message.getBody();

            auditLogService.logOrder(auditLogMapper.toLogOrder(msg));
        } catch (Exception e) {
            log.error("Failed to parse OrderStatusChangedMessage: {}", record.value(), e);
        }
    }
}
