package org.example.audit_service.DAL;

import lombok.RequiredArgsConstructor;
import org.example.audit_service.model.AuditLogOrder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuditLogOrderDAO {
    private final JdbcTemplate jdbcTemplate;

    public Optional<AuditLogOrder> findById(Long id) {
        String sql = "SELECT * FROM audit_log_order WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, getRowMapper(), id));
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<AuditLogOrder> findAll() {
        String sql = "SELECT * FROM audit_log_order ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, getRowMapper());
    }

    public void save(AuditLogOrder auditLogOrder) {
        String sql = """
            INSERT INTO audit_log_order (
                order_id, item_id, customer_id, event_type, 
                created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?)
            """;
        jdbcTemplate.update(sql,
                auditLogOrder.getOrderId(),
                auditLogOrder.getItemId(),
                auditLogOrder.getCustomerId(),
                auditLogOrder.getEventType(),
                Timestamp.valueOf(auditLogOrder.getCreatedAt().toLocalDateTime()),
                Timestamp.valueOf(auditLogOrder.getUpdatedAt().toLocalDateTime()));
    }

    public void saveAll(List<AuditLogOrder> logs) {
        if (logs.isEmpty()) return;

        String sql = """
            INSERT INTO audit_log_order (
                order_id, item_id, customer_id, event_type, 
                created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?)
            """;

        jdbcTemplate.batchUpdate(sql, logs, logs.size(), (ps, log) -> {
            ps.setLong(1, log.getOrderId());
            ps.setLong(2, log.getItemId());
            ps.setLong(3, log.getCustomerId());
            ps.setString(4, log.getEventType());
            ps.setTimestamp(5, Timestamp.valueOf(log.getCreatedAt().toLocalDateTime()));
            ps.setTimestamp(6, Timestamp.valueOf(log.getUpdatedAt().toLocalDateTime()));
        });
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM audit_log_order WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM audit_log_order WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    private RowMapper<AuditLogOrder> getRowMapper() {
        return (rs, rowNum) -> {
            java.sql.Timestamp createdAt = rs.getTimestamp("created_at");
            java.sql.Timestamp updatedAt = rs.getTimestamp("updated_at");
            
            return AuditLogOrder.builder()
                    .id(rs.getLong("id"))
                    .orderId(rs.getLong("order_id"))
                    .itemId(rs.getLong("item_id"))
                    .customerId(rs.getLong("customer_id"))
                    .eventType(rs.getString("event_type"))
                    .createdAt(createdAt != null ? createdAt.toInstant().atOffset(java.time.ZoneOffset.UTC) : null)
                    .updatedAt(updatedAt != null ? updatedAt.toInstant().atOffset(java.time.ZoneOffset.UTC) : null)
                    .build();
        };
    }
}