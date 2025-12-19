package org.example.shop.DAL.orders.repository.jpa;

import org.example.shop.DAL.orders.entity.OrderItem;
import org.example.shop.DAL.orders.repository.jdbc.OrderItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderItemRepository extends JpaRepository<OrderItem, Long>, OrderItemRepositoryCustom {}
