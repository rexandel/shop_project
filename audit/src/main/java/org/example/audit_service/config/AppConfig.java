package org.example.audit_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public NewTopic omsOrderCreatedTopic(@Value("${kafka-settings.oms-order-created-topic}") String topicName) {
        return TopicBuilder.name(topicName)
                .partitions(5)
                .replicas(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, "86400000")
                .build();
    }

    @Bean
    public NewTopic omsOrderStatusChangedTopic(@Value("${kafka-settings.oms-order-status-changed-topic}") String topicName) {
        return TopicBuilder.name(topicName)
                .partitions(5)
                .replicas(1)
                .config(TopicConfig.RETENTION_MS_CONFIG, "86400000")
                .build();
    }
}
