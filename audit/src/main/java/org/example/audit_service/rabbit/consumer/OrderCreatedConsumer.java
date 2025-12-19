package org.example.audit_service.rabbit.consumer;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.audit_service.DTO.AuditLogOrderRequest;
import org.example.audit_service.mappers.AuditLogMapper;
import org.example.audit_service.service.AuditLogService;
import org.example.common.JsonUtil;
import org.example.audit_service.OrderCreatedMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedConsumer implements ChannelAwareMessageListener {

    private final AuditLogService auditLogService;
    private final AuditLogMapper auditLogMapper;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        
        try {
            String json = new String(message.getBody(), StandardCharsets.UTF_8);
            OrderCreatedMessage order = JsonUtil.fromJson(json, OrderCreatedMessage.class);

            if (order.getOrderItems() != null) {
                order.getOrderItems().forEach(item -> {
                    AuditLogOrderRequest.LogOrder logOrder = auditLogMapper.toLogOrder(order, item);
                    auditLogService.logOrder(logOrder);
                });
            }

            channel.basicAck(deliveryTag, false);
            log.info("Message processed and acknowledged. Delivery tag: {}", deliveryTag);
            
        } catch (Exception e) {
            log.error("Error processing message. Delivery tag: {}", deliveryTag, e);
            // Requeue = true, чтобы сообщение вернулось в очередь и мы попытались обработать его снова
            // В сочетании с prefetchCount = 1 это остановит обработку новых сообщений до успеха
            channel.basicNack(deliveryTag, false, true);
            // Не пробрасываем исключение дальше, так как мы уже обработали его через Nack
        }
    }
}
