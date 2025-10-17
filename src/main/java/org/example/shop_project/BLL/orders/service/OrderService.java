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
     * Bulk insert orders with their order items
     */
    @Transactional
    public List<OrderUnit> insertOrdersBulk(List<OrderUnit> units) throws SQLException {
        OffsetDateTime now = OffsetDateTime.now();

        // 1. Преобразуем OrderUnit -> Order
        List<Order> orders = units.stream().map(u -> {
            Order o = new Order();
            o.setCustomerId(u.getCustomerId());
            o.setDeliveryAddress(u.getDeliveryAddress());
            o.setTotalPriceCents(u.getTotalPriceCents());
            o.setTotalPriceCurrency(u.getTotalPriceCurrency());
            o.setCreatedAt(now);
            o.setUpdatedAt(now);
            return o;
        }).toList();

        // 2. Bulk insert orders
        orderRepository.bulkInsert(orders);

        // 3. Преобразуем и привяжем orderItems
        List<OrderItem> items = new ArrayList<>();
        for (int i = 0; i < units.size(); i++) {
            OrderUnit u = units.get(i);
            Order o = orders.get(i);
            for (OrderItemUnit iu : u.getOrderItems()) {
                OrderItem item = new OrderItem();
                item.setOrder(o);
                item.setProductId(iu.getProductId());
                item.setQuantity(iu.getQuantity());
                item.setProductTitle(iu.getProductTitle());
                item.setProductUrl(iu.getProductUrl());
                item.setPriceCents(iu.getPriceCents());
                item.setPriceCurrency(iu.getPriceCurrency());
                item.setCreatedAt(now);
                item.setUpdatedAt(now);
                items.add(item);
            }
        }

        // 4. Bulk insert orderItems
        orderItemRepository.bulkInsert(items);

        // 5. Возвращаем DTO с привязанными orderItems
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

    /**
     * Get orders with optional pagination, filtering and order items
     */
    @Transactional(readOnly = true)
    public List<OrderUnit> getOrders(QueryOrdersDalModel model, boolean includeOrderItems) throws SQLException {
        // 1. Query orders with filters, limit and offset
        List<Order> orders = orderRepository.query(model);

        if (orders.isEmpty()) return List.of();

        // 2. Optionally query order items
        List<OrderItem> items = List.of();
        if (includeOrderItems) {
            Long[] orderIds = orders.stream().map(Order::getId).toArray(Long[]::new);
            QueryOrderItemsDalModel itemModel = new QueryOrderItemsDalModel();
            itemModel.setOrderIds(orderIds);
            itemModel.setLimit(model.getLimit());    // передаем лимит для подзапроса orderItems
            itemModel.setOffset(model.getOffset());  // и смещение
            items = orderItemRepository.query(itemModel);
        }

        // 3. Map order items to orders
        Map<Long, List<OrderItem>> lookup = items.stream()
                .collect(Collectors.groupingBy(i -> i.getOrder().getId()));

        // 4. Map orders -> OrderUnit with attached order items
        return orders.stream().map(o -> {
            OrderUnit unit = new OrderUnit();
            unit.setId(o.getId());
            unit.setCustomerId(o.getCustomerId());
            unit.setDeliveryAddress(o.getDeliveryAddress());
            unit.setTotalPriceCents(o.getTotalPriceCents());
            unit.setTotalPriceCurrency(o.getTotalPriceCurrency());
            unit.setCreatedAt(o.getCreatedAt());
            unit.setUpdatedAt(o.getUpdatedAt());

            // Привязка order items
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

    /**
     * Create orders from V1CreateOrderRequest
     */
    @Transactional
    public V1CreateOrderResponse createOrders(V1CreateOrderRequest request) throws SQLException {
        OffsetDateTime now = OffsetDateTime.now();

        // 1. Преобразуем V1CreateOrderRequest.Order -> Order Entity
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

        // 2. Bulk insert orders
        orderRepository.bulkInsert(orders);

        // 3. Преобразуем и привязываем orderItems
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

        // 4. Bulk insert orderItems
        orderItemRepository.bulkInsert(items);

        // 5. Создаем ответ
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

            // Привязываем order items
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
        // 1. Преобразуем V1QueryOrdersRequest -> QueryOrdersDalModel
        QueryOrdersDalModel dalModel = new QueryOrdersDalModel();
        dalModel.setIds(request.getIds());
        dalModel.setCustomerIds(request.getCustomerIds());
        dalModel.setLimit(request.getLimit());
        dalModel.setOffset(request.getOffset());

        // 2. Получаем заказы с order items
        List<OrderUnit> orderUnits = getOrders(dalModel, true);

        // 3. Преобразуем в V1QueryOrdersResponse
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

            // Преобразуем order items
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

}

