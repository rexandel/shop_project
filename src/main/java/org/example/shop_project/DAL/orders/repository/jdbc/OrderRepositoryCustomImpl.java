package org.example.shop_project.DAL.orders.repository.jdbc;

import org.example.shop_project.DAL.orders.entity.Order;
import org.example.shop_project.DAL.orders.jdbc.UnitOfWork;
import org.example.shop_project.DAL.orders.model.QueryOrdersDalModel;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final UnitOfWork unitOfWork;

    public OrderRepositoryCustomImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @Override
    public List<Order> bulkInsert(List<Order> orders) throws SQLException {
        unitOfWork.bulkInsertOrders(orders);
        return orders;
    }

    @Override
    public List<Order> query(QueryOrdersDalModel model) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT id, customer_id, delivery_address, total_price_cents, total_price_currency, created_at, updated_at FROM orders"
        );

        List<String> conditions = new ArrayList<>();

        try (Connection conn = unitOfWork.getDataSource().getConnection()) {

            int paramIndex = 1;
            if (model.getIds() != null && model.getIds().length > 0) {
                conditions.add("id = ANY(?)");
            }
            if (model.getCustomerIds() != null && model.getCustomerIds().length > 0) {
                conditions.add("customer_id = ANY(?)");
            }

            if (!conditions.isEmpty()) {
                sql.append(" WHERE ").append(String.join(" AND ", conditions));
            }

            if (model.getLimit() > 0) {
                sql.append(" LIMIT ").append(model.getLimit());
            }

            if (model.getOffset() > 0) {
                sql.append(" OFFSET ").append(model.getOffset());
            }

            try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

                // Заполняем массивы PostgreSQL
                if (model.getIds() != null && model.getIds().length > 0) {
                    Array idArray = conn.createArrayOf("BIGINT", model.getIds());
                    ps.setArray(paramIndex++, idArray);
                }
                if (model.getCustomerIds() != null && model.getCustomerIds().length > 0) {
                    Array customerArray = conn.createArrayOf("BIGINT", model.getCustomerIds());
                    ps.setArray(paramIndex++, customerArray);
                }

                ResultSet rs = ps.executeQuery();
                List<Order> orders = new ArrayList<>();
                while (rs.next()) {
                    Order o = new Order();
                    o.setId(rs.getLong("id"));
                    o.setCustomerId(rs.getLong("customer_id"));
                    o.setDeliveryAddress(rs.getString("delivery_address"));
                    o.setTotalPriceCents(rs.getLong("total_price_cents"));
                    o.setTotalPriceCurrency(rs.getString("total_price_currency"));
                    o.setCreatedAt(rs.getObject("created_at", OffsetDateTime.class));
                    o.setUpdatedAt(rs.getObject("updated_at", OffsetDateTime.class));
                    orders.add(o);
                }
                return orders;
            }
        }
    }
}


