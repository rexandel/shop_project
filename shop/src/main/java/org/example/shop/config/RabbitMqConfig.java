package org.example.shop.config;

import org.example.shop.config.properties.RabbitMqProperties;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class RabbitMqConfig {

    private final RabbitMqProperties rabbitMqProperties;

    public RabbitMqConfig(RabbitMqProperties rabbitMqProperties) {
        this.rabbitMqProperties = rabbitMqProperties;
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(rabbitMqProperties.getExchange());
    }

    @Bean
    public Declarables bindings(TopicExchange exchange) {
        List<org.springframework.amqp.core.Declarable> declarables = new ArrayList<>();

        if (rabbitMqProperties.getExchangeMappings() != null) {
            for (RabbitMqProperties.ExchangeMapping mapping : rabbitMqProperties.getExchangeMappings()) {
                Map<String, Object> args = null;
                if (mapping.getDeadLetter() != null) {
                    args = new HashMap<>();
                    args.put("x-dead-letter-exchange", mapping.getDeadLetter().getDlx());
                    args.put("x-dead-letter-routing-key", mapping.getDeadLetter().getRoutingKey());
                }

                Queue queue = new Queue(mapping.getQueue(), false, false, false, args);
                declarables.add(queue);
                declarables.add(BindingBuilder.bind(queue).to(exchange).with(mapping.getRoutingKeyPattern()));
            }
        }

        return new Declarables(declarables);
    }
}
