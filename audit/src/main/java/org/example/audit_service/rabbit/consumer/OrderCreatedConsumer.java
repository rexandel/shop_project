package org.example.audit_service.rabbit.consumer;

import lombok.RequiredArgsConstructor;
import org.example.audit_service.DTO.AuditLogOrderRequest;
import org.example.common.JsonUtil;
import org.example.audit_service.OrderCreatedMessage;
import org.example.client.OmsClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderCreatedConsumer implements MessageListener {

    private final OmsClient omsClient;

    @Override
    public void onMessage(Message message) {
        String json = new String(message.getBody(), StandardCharsets.UTF_8);
        OrderCreatedMessage order = JsonUtil.fromJson(json, OrderCreatedMessage.class);

        List<AuditLogOrderRequest.LogOrder> logs = order.getOrderItems().stream()
                .map(item -> new AuditLogOrderRequest.LogOrder(
                        order.getId(),
                        item.getId(),
                        order.getCustomerId(),
                        "CREATED"
                ))
                .toList();

        AuditLogOrderRequest request = new AuditLogOrderRequest();
        request.setOrders(logs);
        omsClient.logOrder(request);
    }
}
