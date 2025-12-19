package org.example.audit_service.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMqProperties {

    private String host;
    private int port;
    private String orderCreatedQueue;
    private Integer batchSize = 100;
    private Integer batchTimeoutSeconds = 1;
}