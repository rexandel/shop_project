package org.example.shop_project.DAL.orders.repository.jdbc;

import org.example.shop_project.DAL.orders.entity.OrderItem;
import org.example.shop_project.DAL.orders.model.QueryOrderItemsDalModel;

import java.sql.SQLException;
import java.util.List;


public interface OrderItemRepositoryCustom {
    List<OrderItem> bulkInsert(List<OrderItem> items) throws SQLException;

    List<OrderItem> query(QueryOrderItemsDalModel model) throws SQLException;
}
