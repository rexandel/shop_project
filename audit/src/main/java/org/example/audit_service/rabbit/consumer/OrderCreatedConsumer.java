package org.example.audit_service.rabbit.consumer;

import lombok.RequiredArgsConstructor;
import org.example.audit_service.DTO.AuditLogOrderRequest;
import org.example.audit_service.service.AuditLogService;
import org.example.common.JsonUtil;
import org.example.audit_service.OrderCreatedMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderCreatedConsumer implements MessageListener {

    private final AuditLogService auditLogService;

    @Override
    public void onMessage(Message message) {
        String json = new String(message.getBody(), StandardCharsets.UTF_8);
        OrderCreatedMessage order = JsonUtil.fromJson(json, OrderCreatedMessage.class);

        // Логируем каждый item заказа
        if (order.getOrderItems() != null) {
            order.getOrderItems().forEach(item -> {
                AuditLogOrderRequest.LogOrder logOrder = new AuditLogOrderRequest.LogOrder(
                        order.getId(),
                        item.getId(),
                        order.getCustomerId(),
                        item.getProductId(),
                        item.getQuantity(),
                        item.getProductTitle(),
                        item.getProductUrl(),
                        order.getDeliveryAddress(),
                        order.getTotalPriceCents(),
                        order.getTotalPriceCurrency(),
                        item.getPriceCents(),
                        item.getPriceCurrency(),
                        "ORDER_CREATED"
                );
                auditLogService.logOrder(logOrder);
            });
        }
    }
}
