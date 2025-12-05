package org.example.client;

import lombok.RequiredArgsConstructor;
import org.example.audit_service.DTO.AuditLogOrderRequest;
import org.example.audit_service.service.AuditLogService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OmsClient {

    private final AuditLogService auditLogService;

    public void logOrder(AuditLogOrderRequest request) {
        if (request.getOrders() == null || request.getOrders().isEmpty()) {
            return;
        }

        for (AuditLogOrderRequest.LogOrder order : request.getOrders()) {
            auditLogService.logOrder(
                    order.getOrderId(),
                    order.getOrderItemId(),
                    order.getCustomerId(),
                    order.getOrderStatus()
            );
        }
    }
}
