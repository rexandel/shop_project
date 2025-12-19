package org.example.shop.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kafka-settings")
public class KafkaSettings {
    private String bootstrapServers;
    private String clientId;
    private String omsOrderCreatedTopic;
    private String omsOrderStatusChangedTopic;
}
