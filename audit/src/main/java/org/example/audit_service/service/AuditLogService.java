package org.example.audit_service.service;

import lombok.RequiredArgsConstructor;
import org.example.audit_service.DAL.AuditLogOrderDAO;
import org.example.audit_service.DTO.AuditLogOrderRequest;
import org.example.audit_service.mappers.AuditLogMapper;
import org.example.audit_service.model.AuditLogOrder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogOrderDAO auditLogOrderDAO;
    private final AuditLogMapper auditLogMapper;

    public void logOrder(AuditLogOrderRequest.LogOrder logOrder) {
        // DTO -> Entity
        AuditLogOrder log = auditLogMapper.toEntity(logOrder);
        auditLogOrderDAO.save(log);
    }

    public void logOrders(java.util.List<AuditLogOrderRequest.LogOrder> logOrders) {
        java.util.List<AuditLogOrder> logs = logOrders.stream()
                .map(auditLogMapper::toEntity)
                .toList();
        auditLogOrderDAO.saveAll(logs);
    }
}