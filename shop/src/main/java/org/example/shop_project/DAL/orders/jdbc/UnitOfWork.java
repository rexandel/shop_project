package org.example.shop_project.DAL.orders.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.sql.DataSource;
import org.example.shop_project.DAL.orders.entity.Order;
import org.example.shop_project.DAL.orders.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class UnitOfWork {

  private final DataSource dataSource;

  public UnitOfWork(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void bulkInsertOrders(List<Order> orders) throws SQLException {
    String sql =
        "INSERT INTO orders (customer_id, delivery_address, total_price_cents,"
            + " total_price_currency, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps =
            conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      for (Order o : orders) {
        ps.setLong(1, o.getCustomerId());
        ps.setString(2, o.getDeliveryAddress());
        ps.setLong(3, o.getTotalPriceCents());
        ps.setString(4, o.getTotalPriceCurrency());
        ps.setObject(5, o.getCreatedAt());
        ps.setObject(6, o.getUpdatedAt());
        ps.addBatch();
      }

      ps.executeBatch();

      try (ResultSet keys = ps.getGeneratedKeys()) {
        int index = 0;
        while (keys.next()) {
          orders.get(index++).setId(keys.getLong(1));
        }
      }
    }
  }

  public void bulkInsertOrderItems(List<OrderItem> items) throws SQLException {
    String sql =
        "INSERT INTO order_items (order_id, product_id, quantity, product_title,"
            + " product_url, price_cents, price_currency, created_at, updated_at) VALUES (?, ?,"
            + " ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = dataSource.getConnection();
        PreparedStatement ps =
            conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      for (OrderItem i : items) {
        ps.setLong(1, i.getOrder().getId());
        ps.setLong(2, i.getProductId());
        ps.setInt(3, i.getQuantity());
        ps.setString(4, i.getProductTitle());
        ps.setString(5, i.getProductUrl());
        ps.setLong(6, i.getPriceCents());
        ps.setString(7, i.getPriceCurrency());
        ps.setObject(8, i.getCreatedAt());
        ps.setObject(9, i.getUpdatedAt());
        ps.addBatch();
      }

      ps.executeBatch();

      try (ResultSet keys = ps.getGeneratedKeys()) {
        int index = 0;
        while (keys.next()) {
          items.get(index++).setId(keys.getLong(1));
        }
      }
    }
  }

  public DataSource getDataSource() {
    return dataSource;
  }
}


