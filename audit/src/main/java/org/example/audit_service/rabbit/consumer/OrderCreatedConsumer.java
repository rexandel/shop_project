package org.example.audit_service.rabbit.consumer;

import lombok.extern.slf4j.Slf4j;
import org.example.audit_service.DTO.AuditLogOrderRequest;
import org.example.audit_service.OrderCreatedMessage;
import org.example.audit_service.mappers.AuditLogMapper;
import org.example.audit_service.rabbit.consumer.base.BaseBatchMessageConsumer;
import org.example.audit_service.service.AuditLogService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class OrderCreatedConsumer extends BaseBatchMessageConsumer<OrderCreatedMessage> {

    private final AuditLogService auditLogService;
    private final AuditLogMapper auditLogMapper;

    public OrderCreatedConsumer(AuditLogService auditLogService, AuditLogMapper auditLogMapper) {
        super(OrderCreatedMessage.class);
        this.auditLogService = auditLogService;
        this.auditLogMapper = auditLogMapper;
    }

    @Override
    protected void processMessages(List<OrderCreatedMessage> messages) {
        List<AuditLogOrderRequest.LogOrder> allLogOrders = new ArrayList<>();
        for (OrderCreatedMessage order : messages) {
            if (order.getOrderItems() != null) {
                order.getOrderItems().forEach(item -> {
                    allLogOrders.add(auditLogMapper.toLogOrder(order, item));
                });
            }
        }
        if (!allLogOrders.isEmpty()) {
            auditLogService.logOrders(allLogOrders);
        }
    }
}
