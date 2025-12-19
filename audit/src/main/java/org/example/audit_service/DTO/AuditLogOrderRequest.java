package org.example.audit_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class AuditLogOrderRequest {
    private List<LogOrder> orders;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogOrder {
        private Long orderId;
        private Long itemId;
        private Long customerId;
        private String eventType;
    }
}
