package org.example.audit_service.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kafka-settings")
public class KafkaSettings {
    private String bootstrapServers;
    private String groupId;
    private String omsOrderCreatedTopic;
    private String omsOrderStatusChangedTopic;
}
