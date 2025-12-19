package org.example.shop.kafka;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.shop.common.JsonUtil;
import org.example.shop.config.properties.KafkaSettings;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaSettings kafkaSettings;
    private org.apache.kafka.clients.producer.KafkaProducer<String, String> producer;

    @PostConstruct
    public void init() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaSettings.getBootstrapServers());
        config.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaSettings.getClientId());
        config.put(ProducerConfig.LINGER_MS_CONFIG, 100);
        config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        // Partitioner.Consistent is default in Java client (DefaultPartitioner)
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 240000);
        config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 60000);

        this.producer = new org.apache.kafka.clients.producer.KafkaProducer<>(config);
    }

    @PreDestroy
    public void close() {
        if (producer != null) {
            producer.close();
        }
    }

    public <T> void produce(String topic, List<KafkaMessage<T>> messages) {
        List<CompletableFuture<RecordMetadata>> futures = messages.stream()
            .map(msg -> {
                CompletableFuture<RecordMetadata> future = new CompletableFuture<>();
                try {
                    String json = JsonUtil.toJson(msg.getMessage());
                    producer.send(new ProducerRecord<>(topic, msg.getKey(), json), (metadata, exception) -> {
                        if (exception != null) {
                            log.error("Failed to send message: {}", exception.getMessage());
                            future.completeExceptionally(exception);
                        } else {
                            future.complete(metadata);
                        }
                    });
                } catch (Exception e) {
                    log.error("Failed to send message: {}", e.getMessage());
                    future.completeExceptionally(e);
                }
                return future;
            })
            .collect(Collectors.toList());

        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
             throw new RuntimeException("Failed to produce messages", e);
        }
    }
}
