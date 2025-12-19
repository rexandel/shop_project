package org.example.audit_service.config;

import lombok.RequiredArgsConstructor;
import org.example.audit_service.config.property.RabbitMqProperties;
import org.example.audit_service.rabbit.consumer.OrderCreatedConsumer;
import org.example.audit_service.rabbit.consumer.OrderStatusChangedConsumer;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {

    private final RabbitMqProperties settings;

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(settings.getOrderCreated().getDeadLetter().getDlx());
    }

    @Bean
    public Queue orderCreatedDlq() {
        return new Queue(settings.getOrderCreated().getDeadLetter().getDlq(), true);
    }

    @Bean
    public Binding orderCreatedDlqBinding() {
        return BindingBuilder.bind(orderCreatedDlq())
                .to(deadLetterExchange())
                .with(settings.getOrderCreated().getDeadLetter().getRoutingKey());
    }

    @Bean
    public Queue orderStatusChangedDlq() {
        return new Queue(settings.getOrderStatusChanged().getDeadLetter().getDlq(), true);
    }

    @Bean
    public Binding orderStatusChangedDlqBinding() {
        return BindingBuilder.bind(orderStatusChangedDlq())
                .to(deadLetterExchange())
                .with(settings.getOrderStatusChanged().getDeadLetter().getRoutingKey());
    }

    @Bean
    public Queue orderCreatedQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", settings.getOrderCreated().getDeadLetter().getDlx());
        args.put("x-dead-letter-routing-key", settings.getOrderCreated().getDeadLetter().getRoutingKey());
        return new Queue(settings.getOrderCreated().getQueue(), false, false, false, args);
    }

    @Bean
    public Queue orderStatusChangedQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", settings.getOrderStatusChanged().getDeadLetter().getDlx());
        args.put("x-dead-letter-routing-key", settings.getOrderStatusChanged().getDeadLetter().getRoutingKey());
        return new Queue(settings.getOrderStatusChanged().getQueue(), false, false, false, args);
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
