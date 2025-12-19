package org.example.shop.rabbit;

import lombok.RequiredArgsConstructor;
import org.example.shop.common.JsonUtil;
import org.example.shop.config.properties.RabbitMqProperties;
import org.example.shop.rabbit.message.OrderCreatedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RabbitMqProducer {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqProperties rabbitMqProperties;

    public void publish(List<OrderCreatedMessage> messages, String queue) {
        for (Object msg : messages) {
            String json = JsonUtil.toJson(msg);
            rabbitTemplate.convertAndSend(queue, json);
        }
    }

    public void publishOrderCreated(List<OrderCreatedMessage> messages) {
        publish(messages, rabbitMqProperties.getOrderCreatedQueue());
    }
}
