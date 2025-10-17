package org.example.shop_project.DAL.orders.repository.jdbc;

import org.example.shop_project.DAL.orders.entity.Order;
import org.example.shop_project.DAL.orders.model.QueryOrdersDalModel;

import java.sql.SQLException;
import java.util.List;

public interface OrderRepositoryCustom {
    List<Order> bulkInsert(List<Order> orders) throws SQLException;

    List<Order> query(QueryOrdersDalModel model) throws SQLException;
}