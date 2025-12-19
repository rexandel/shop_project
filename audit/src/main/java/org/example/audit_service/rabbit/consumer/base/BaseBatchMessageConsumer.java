package org.example.audit_service.rabbit.consumer.base;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.example.common.JsonUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareBatchMessageListener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class BaseBatchMessageConsumer<T> implements ChannelAwareBatchMessageListener {

    private final Class<T> type;

    protected BaseBatchMessageConsumer(Class<T> type) {
        this.type = type;
    }

    protected abstract void processMessages(List<T> messages);

    @Override
    public void onMessageBatch(List<Message> messages, Channel channel) {
        if (messages.isEmpty()) return;

        List<T> batch = new ArrayList<>();
        List<MessageInfo> messageInfos = new ArrayList<>();

        try {
            for (Message message : messages) {
                String body = new String(message.getBody(), StandardCharsets.UTF_8);
                long deliveryTag = message.getMessageProperties().getDeliveryTag();
                
                messageInfos.add(new MessageInfo(body, deliveryTag, OffsetDateTime.now()));
                
                T dto = JsonUtil.fromJson(body, type);
                batch.add(dto);
            }

            processMessages(batch);

            long lastDeliveryTag = messageInfos.get(messageInfos.size() - 1).getDeliveryTag();
            channel.basicAck(lastDeliveryTag, true);
            
            log.info("Successfully processed batch of {} messages", messages.size());

        } catch (Exception ex) {
            log.error("Failed to process batch: {}", ex.getMessage());
            
            try {
                if (!messageInfos.isEmpty()) {
                    long lastDeliveryTag = messageInfos.get(messageInfos.size() - 1).getDeliveryTag();
                    channel.basicNack(lastDeliveryTag, true, false);
                } else if (!messages.isEmpty()) {
                     long lastDeliveryTag = messages.get(messages.size() - 1).getMessageProperties().getDeliveryTag();
                     channel.basicNack(lastDeliveryTag, true, false);
                }
            } catch (IOException e) {
                log.error("Failed to NACK batch", e);
            }
        }
    }
}
