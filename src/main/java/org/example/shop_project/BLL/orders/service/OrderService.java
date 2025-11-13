package org.example.shop_project.BLL.orders.service;

import org.example.shop_project.BLL.orders.models.OrderItemUnit;
import org.example.shop_project.BLL.orders.models.OrderUnit;
import org.example.shop_project.DAL.orders.entity.Order;
import org.example.shop_project.DAL.orders.entity.OrderItem;
import org.example.shop_project.DAL.orders.model.QueryOrdersDalModel;
import org.example.shop_project.DAL.orders.repository.jpa.IOrderItemRepository;
import org.example.shop_project.DAL.orders.repository.jpa.IOrderRepository;
import org.example.shop_project.DTO.requests.V1CreateOrderRequest;
import org.example.shop_project.DTO.requests.V1QueryOrdersRequest;
import org.example.shop_project.DTO.responses.V1CreateOrderResponse;
import org.example.shop_project.DTO.responses.V1QueryOrdersResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.example.shop_project.DAL.orders.model.QueryOrderItemsDalModel;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final IOrderRepository orderRepository;
    private final IOrderItemRepository orderItemRepository;

    public OrderService(IOrderRepository orderRepository, IOrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * Create orders from V1CreateOrderRequest
     */
    @Transactional
    public V1CreateOrderResponse createOrders(V1CreateOrderRequest request) throws SQLException {
        OffsetDateTime now = OffsetDateTime.now();

        List<Order> orders = request.getOrders().stream().map(reqOrder -> {
            Order order = new Order();
            order.setCustomerId(reqOrder.getCustomerId());
            order.setDeliveryAddress(reqOrder.getDeliveryAddress());
            order.setTotalPriceCents(reqOrder.getTotalPriceCents());
            order.setTotalPriceCurrency(reqOrder.getTotalPriceCurrency());
            order.setCreatedAt(now);
            order.setUpdatedAt(now);
            return order;
        }).toList();

        orderRepository.bulkInsert(orders);

        List<OrderItem> items = new ArrayList<>();
        for (int i = 0; i < request.getOrders().size(); i++) {
            V1CreateOrderRequest.Order reqOrder = request.getOrders().get(i);
            Order order = orders.get(i);
            
            for (V1CreateOrderRequest.OrderItem reqItem : reqOrder.getOrderItems()) {
                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProductId(reqItem.getProductId());
                item.setQuantity(reqItem.getQuantity());
                item.setProductTitle(reqItem.getProductTitle());
                item.setProductUrl(reqItem.getProductUrl());
                item.setPriceCents(reqItem.getPriceCents());
                item.setPriceCurrency(reqItem.getPriceCurrency());
                item.setCreatedAt(now);
                item.setUpdatedAt(now);
                items.add(item);
            }
        }

        orderItemRepository.bulkInsert(items);

        V1CreateOrderResponse response = new V1CreateOrderResponse();
        List<V1CreateOrderResponse.Order> responseOrders = orders.stream().map(order -> {
            V1CreateOrderResponse.Order respOrder = new V1CreateOrderResponse.Order();
            respOrder.setId(order.getId());
            respOrder.setCustomerId(order.getCustomerId());
            respOrder.setDeliveryAddress(order.getDeliveryAddress());
            respOrder.setTotalPriceCents(order.getTotalPriceCents());
            respOrder.setTotalPriceCurrency(order.getTotalPriceCurrency());
            respOrder.setCreatedAt(order.getCreatedAt());
            respOrder.setUpdatedAt(order.getUpdatedAt());

            List<V1CreateOrderResponse.OrderItem> respItems = items.stream()
                    .filter(item -> item.getOrder().getId().equals(order.getId()))
                    .map(item -> {
                        V1CreateOrderResponse.OrderItem respItem = new V1CreateOrderResponse.OrderItem();
                        respItem.setId(item.getId());
                        respItem.setOrderId(order.getId());
                        respItem.setProductId(item.getProductId());
                        respItem.setQuantity(item.getQuantity());
                        respItem.setProductTitle(item.getProductTitle());
                        respItem.setProductUrl(item.getProductUrl());
                        respItem.setPriceCents(item.getPriceCents());
                        respItem.setPriceCurrency(item.getPriceCurrency());
                        respItem.setCreatedAt(item.getCreatedAt());
                        respItem.setUpdatedAt(item.getUpdatedAt());
                        return respItem;
                    }).toList();

            respOrder.setOrderItems(respItems);
            return respOrder;
        }).toList();

        response.setOrders(responseOrders);
        return response;
    }

    /**
     * Query orders from V1QueryOrdersRequest
     */
    @Transactional(readOnly = true)
    public V1QueryOrdersResponse queryOrders(V1QueryOrdersRequest request) throws SQLException {
        QueryOrdersDalModel dalModel = new QueryOrdersDalModel();
        dalModel.setIds(request.getIds());
        dalModel.setCustomerIds(request.getCustomerIds());
        dalModel.setLimit(request.getLimit());
        dalModel.setOffset(request.getOffset());

        List<OrderUnit> orderUnits = getOrders(dalModel, true);

        V1QueryOrdersResponse response = new V1QueryOrdersResponse();
        List<V1QueryOrdersResponse.Order> responseOrders = orderUnits.stream().map(orderUnit -> {
            V1QueryOrdersResponse.Order respOrder = new V1QueryOrdersResponse.Order();
            respOrder.setId(orderUnit.getId());
            respOrder.setCustomerId(orderUnit.getCustomerId());
            respOrder.setDeliveryAddress(orderUnit.getDeliveryAddress());
            respOrder.setTotalPriceCents(orderUnit.getTotalPriceCents());
            respOrder.setTotalPriceCurrency(orderUnit.getTotalPriceCurrency());
            respOrder.setCreatedAt(orderUnit.getCreatedAt());
            respOrder.setUpdatedAt(orderUnit.getUpdatedAt());

            List<V1QueryOrdersResponse.OrderItem> respItems = orderUnit.getOrderItems().stream().map(itemUnit -> {
                V1QueryOrdersResponse.OrderItem respItem = new V1QueryOrdersResponse.OrderItem();
                respItem.setId(itemUnit.getId());
                respItem.setOrderId(itemUnit.getOrderId());
                respItem.setProductId(itemUnit.getProductId());
                respItem.setQuantity(itemUnit.getQuantity());
                respItem.setProductTitle(itemUnit.getProductTitle());
                respItem.setProductUrl(itemUnit.getProductUrl());
                respItem.setPriceCents(itemUnit.getPriceCents());
                respItem.setPriceCurrency(itemUnit.getPriceCurrency());
                respItem.setCreatedAt(itemUnit.getCreatedAt());
                respItem.setUpdatedAt(itemUnit.getUpdatedAt());
                return respItem;
            }).toList();

            respOrder.setOrderItems(respItems);
            return respOrder;
        }).toList();

        response.setOrders(responseOrders);
        return response;
    }


    @Transactional(readOnly = true)
    public List<OrderUnit> getOrders(QueryOrdersDalModel model, boolean includeOrderItems) throws SQLException {
        List<Order> orders = orderRepository.query(model);

        if (orders.isEmpty()) return List.of();

        List<OrderItem> items = List.of();
        if (includeOrderItems) {
            Long[] orderIds = orders.stream().map(Order::getId).toArray(Long[]::new);
            QueryOrderItemsDalModel itemModel = new QueryOrderItemsDalModel();
            itemModel.setOrderIds(orderIds);
            itemModel.setLimit(model.getLimit());
            itemModel.setOffset(model.getOffset());
            items = orderItemRepository.query(itemModel);
        }

        Map<Long, List<OrderItem>> lookup = items.stream()
                .collect(Collectors.groupingBy(i -> i.getOrder().getId()));

        return orders.stream().map(o -> {
            OrderUnit unit = new OrderUnit();
            unit.setId(o.getId());
            unit.setCustomerId(o.getCustomerId());
            unit.setDeliveryAddress(o.getDeliveryAddress());
            unit.setTotalPriceCents(o.getTotalPriceCents());
            unit.setTotalPriceCurrency(o.getTotalPriceCurrency());
            unit.setCreatedAt(o.getCreatedAt());
            unit.setUpdatedAt(o.getUpdatedAt());

            unit.setOrderItems(
                    lookup.getOrDefault(o.getId(), List.of()).stream().map(i -> {
                        OrderItemUnit iu = new OrderItemUnit();
                        iu.setId(i.getId());
                        iu.setOrderId(o.getId());
                        iu.setProductId(i.getProductId());
                        iu.setQuantity(i.getQuantity());
                        iu.setProductTitle(i.getProductTitle());
                        iu.setProductUrl(i.getProductUrl());
                        iu.setPriceCents(i.getPriceCents());
                        iu.setPriceCurrency(i.getPriceCurrency());
                        iu.setCreatedAt(i.getCreatedAt());
                        iu.setUpdatedAt(i.getUpdatedAt());
                        return iu;
                    }).toList()
            );

            return unit;
        }).toList();
    }
}

