package org.example.shop_project.DAL.orders.repository.jdbc;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.example.shop_project.DAL.orders.entity.Order;
import org.example.shop_project.DAL.orders.entity.OrderItem;
import org.example.shop_project.DAL.orders.jdbc.UnitOfWork;
import org.example.shop_project.DAL.orders.model.QueryOrderItemsDalModel;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemRepositoryCustomImpl implements OrderItemRepositoryCustom {

  private final UnitOfWork unitOfWork;

  public OrderItemRepositoryCustomImpl(UnitOfWork unitOfWork) {
    this.unitOfWork = unitOfWork;
  }

  @Override
  public List<OrderItem> bulkInsert(List<OrderItem> items) throws SQLException {
    unitOfWork.bulkInsertOrderItems(items);
    return items;
  }

  @Override
  public List<OrderItem> query(QueryOrderItemsDalModel model) throws SQLException {
    StringBuilder sql =
        new StringBuilder(
            "SELECT id, order_id, product_id, quantity, product_title, product_url,"
                + " price_cents, price_currency, created_at, updated_at FROM order_items");

    List<String> conditions = new ArrayList<>();

    try (Connection conn = unitOfWork.getDataSource().getConnection()) {

      int paramIndex = 1;
      if (model.getIds() != null && model.getIds().length > 0) {
        conditions.add("id = ANY(?)");
      }
      if (model.getOrderIds() != null && model.getOrderIds().length > 0) {
        conditions.add("order_id = ANY(?)");
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

        if (model.getIds() != null && model.getIds().length > 0) {
          Array idArray = conn.createArrayOf("BIGINT", model.getIds());
          ps.setArray(paramIndex++, idArray);
        }
        if (model.getOrderIds() != null && model.getOrderIds().length > 0) {
          Array orderArray = conn.createArrayOf("BIGINT", model.getOrderIds());
          ps.setArray(paramIndex++, orderArray);
        }

        ResultSet rs = ps.executeQuery();
        List<OrderItem> items = new ArrayList<>();
        while (rs.next()) {
          OrderItem i = new OrderItem();
          i.setId(rs.getLong("id"));

          Order order = new Order();
          order.setId(rs.getLong("order_id"));
          i.setOrder(order);

          i.setProductId(rs.getLong("product_id"));
          i.setQuantity(rs.getInt("quantity"));
          i.setProductTitle(rs.getString("product_title"));
          i.setProductUrl(rs.getString("product_url"));
          i.setPriceCents(rs.getLong("price_cents"));
          i.setPriceCurrency(rs.getString("price_currency"));
          i.setCreatedAt(rs.getObject("created_at", OffsetDateTime.class));
          i.setUpdatedAt(rs.getObject("updated_at", OffsetDateTime.class));
          items.add(i);
        }
        return items;
      }
    }
  }
}
