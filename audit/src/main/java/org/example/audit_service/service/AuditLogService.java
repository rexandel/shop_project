package org.example.audit_service.service;

import lombok.RequiredArgsConstructor;
import org.example.audit_service.DAL.AuditLogOrderDAO;
import org.example.audit_service.model.AuditLogOrder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogOrderDAO auditLogOrderDAO;

    public void logOrder(Long orderId, Long orderItemId, Long customerId, String orderStatus) {
        AuditLogOrder log = new AuditLogOrder();
        log.setOrderId(orderId);
        log.setOrderItemId(orderItemId);
        log.setCustomerId(customerId);
        log.setOrderStatus(orderStatus);
        log.setCreatedAt(OffsetDateTime.now());
        log.setUpdatedAt(OffsetDateTime.now());

        auditLogOrderDAO.save(log);
    }
}