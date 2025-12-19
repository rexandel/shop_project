package org.example.audit_service.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMqProperties {

    private String host;
    private int port;
    private TopicSettingsUnit orderCreated;
    private TopicSettingsUnit orderStatusChanged;

    @Data
    public static class TopicSettingsUnit {
        private String queue;
        private Integer batchSize;
        private Integer batchTimeoutSeconds;
        private DeadLetterSettings deadLetter;
    }

    @Data
    public static class DeadLetterSettings {
        private String dlx;
        private String dlq;
        private String routingKey;
    }
}