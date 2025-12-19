package org.example.audit_service.rabbit.consumer.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageInfo {
    private String message;
    private long deliveryTag;
    private OffsetDateTime receivedAt;
}
