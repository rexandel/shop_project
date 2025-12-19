package org.example.shop.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaMessage<T> {
    private String key;
    private T message;
}
