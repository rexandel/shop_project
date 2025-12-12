package org.example.audit_service.config;

import lombok.RequiredArgsConstructor;
import org.example.audit_service.config.property.RabbitMqProperties;
import org.example.audit_service.rabbit.consumer.OrderCreatedConsumer;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {

    private final RabbitMqProperties settings;

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(settings.getOrderCreatedQueue(), false);
    }

    @Bean
    public SimpleMessageListenerContainer listenerContainer(ConnectionFactory connectionFactory,
                                                            OrderCreatedConsumer consumer) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(orderCreatedQueue());
        container.setMessageListener(consumer);
        // Настраиваем ручное подтверждение (Ack)
        container.setAcknowledgeMode(org.springframework.amqp.core.AcknowledgeMode.MANUAL);
        container.setPrefetchCount(10); // Количество сообщений для предварительной загрузки
        return container;
    }

    @Bean
    public CachingConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(settings.getHost());
        factory.setPort(settings.getPort());
        return factory;
    }
}
