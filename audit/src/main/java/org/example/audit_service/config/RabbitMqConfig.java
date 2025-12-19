package org.example.audit_service.config;

import lombok.RequiredArgsConstructor;
import org.example.audit_service.config.property.RabbitMqProperties;
import org.example.audit_service.rabbit.consumer.OrderCreatedConsumer;
import org.example.audit_service.rabbit.consumer.OrderStatusChangedConsumer;
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
        return new Queue(settings.getOrderCreated().getQueue(), false);
    }

    @Bean
    public Queue orderStatusChangedQueue() {
        return new Queue(settings.getOrderStatusChanged().getQueue(), false);
    }

    @Bean
    public SimpleMessageListenerContainer orderCreatedListenerContainer(ConnectionFactory connectionFactory,
                                                            OrderCreatedConsumer consumer) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(orderCreatedQueue());
        container.setMessageListener(consumer);
        
        container.setAcknowledgeMode(org.springframework.amqp.core.AcknowledgeMode.MANUAL);
        
        container.setConsumerBatchEnabled(true);
        container.setBatchSize(settings.getOrderCreated().getBatchSize());
        container.setReceiveTimeout((long) settings.getOrderCreated().getBatchTimeoutSeconds() * 1000);
        
        container.setPrefetchCount(settings.getOrderCreated().getBatchSize()); 
        
        return container;
    }

    @Bean
    public SimpleMessageListenerContainer orderStatusChangedListenerContainer(ConnectionFactory connectionFactory,
                                                                        OrderStatusChangedConsumer consumer) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(orderStatusChangedQueue());
        container.setMessageListener(consumer);

        container.setAcknowledgeMode(org.springframework.amqp.core.AcknowledgeMode.MANUAL);

        container.setConsumerBatchEnabled(true);
        container.setBatchSize(settings.getOrderStatusChanged().getBatchSize());
        container.setReceiveTimeout((long) settings.getOrderStatusChanged().getBatchTimeoutSeconds() * 1000);

        container.setPrefetchCount(settings.getOrderStatusChanged().getBatchSize());

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
