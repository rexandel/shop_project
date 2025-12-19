package org.example.audit_service.rabbit.consumer;

import lombok.extern.slf4j.Slf4j;
import org.example.audit_service.DTO.AuditLogOrderRequest;
import org.example.audit_service.OrderStatusChangedMessage;
import org.example.audit_service.mappers.AuditLogMapper;
import org.example.audit_service.rabbit.consumer.base.BaseBatchMessageConsumer;
import org.example.audit_service.service.AuditLogService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class OrderStatusChangedConsumer extends BaseBatchMessageConsumer<OrderStatusChangedMessage> {

    private final AuditLogService auditLogService;
    private final AuditLogMapper auditLogMapper;

    public OrderStatusChangedConsumer(AuditLogService auditLogService, AuditLogMapper auditLogMapper) {
        super(OrderStatusChangedMessage.class);
        this.auditLogService = auditLogService;
        this.auditLogMapper = auditLogMapper;
    }

    @Override
    protected void processMessages(List<OrderStatusChangedMessage> messages) {
        List<AuditLogOrderRequest.LogOrder> allLogOrders = new ArrayList<>();
        for (OrderStatusChangedMessage msg : messages) {
            allLogOrders.add(auditLogMapper.toLogOrder(msg));
        }
        if (!allLogOrders.isEmpty()) {
            auditLogService.logOrders(allLogOrders);
        }
    }
}
