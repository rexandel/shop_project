package org.example.shop_project.DAL.orders.repository.jpa;

import org.example.shop_project.DAL.orders.entity.Order;
import org.example.shop_project.DAL.orders.repository.jdbc.OrderRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IOrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {}
