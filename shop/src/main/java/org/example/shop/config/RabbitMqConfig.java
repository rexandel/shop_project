package org.example.shop.config;

import org.example.shop.config.properties.RabbitMqProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    private final RabbitMqProperties rabbitMqProperties;

    public RabbitMqConfig(RabbitMqProperties rabbitMqProperties) {
        this.rabbitMqProperties = rabbitMqProperties;
    }

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(rabbitMqProperties.getOrderCreatedQueue(), false);
    }
}
