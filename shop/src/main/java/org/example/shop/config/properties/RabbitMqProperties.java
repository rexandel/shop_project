package org.example.shop.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMqProperties {

    private String host;
    private int port;
    private String exchange;
    private List<ExchangeMapping> exchangeMappings;

    @Data
    public static class ExchangeMapping {
        private String queue;
        private String routingKeyPattern;
        private DeadLetterSettings deadLetter;
    }

    @Data
    public static class DeadLetterSettings {
        private String dlx;
        private String routingKey;
    }
}