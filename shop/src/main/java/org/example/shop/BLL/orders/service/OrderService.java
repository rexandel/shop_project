package org.example.shop.BLL.orders.service;

import lombok.RequiredArgsConstructor;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.example.shop.BLL.orders.models.OrderUnit;
import org.example.shop.DAL.orders.entity.Order;
import org.example.shop.DAL.orders.entity.OrderItem;
import org.example.shop.DAL.orders.model.QueryOrderItemsDalModel;
import org.example.shop.DAL.orders.model.QueryOrdersDalModel;
import org.example.shop.DAL.orders.repository.jpa.IOrderItemRepository;
import org.example.shop.DAL.orders.repository.jpa.IOrderRepository;
import org.example.shop.DTO.requests.V1CreateOrderRequest;
import org.example.shop.DTO.requests.V1QueryOrdersRequest;
import org.example.shop.DTO.requests.V1UpdateOrdersStatusRequest;
import org.example.shop.DTO.responses.V1CreateOrderResponse;
import org.example.shop.DTO.responses.V1QueryOrdersResponse;
import org.example.shop.mappers.OrderMapper;
import org.example.shop.rabbit.RabbitMqProducer;
import org.example.shop.rabbit.message.OrderCreatedMessage;
import org.example.shop.rabbit.message.OrderStatusChangedMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final IOrderRepository orderRepository;
  private final IOrderItemRepository orderItemRepository;
  private final RabbitMqProducer rabbitMqProducer;
  private final OrderMapper orderMapper;

  @Transactional
  public V1CreateOrderResponse createOrders(V1CreateOrderRequest request)
      throws SQLException {
    OffsetDateTime now = OffsetDateTime.now();

    List<Order> orders = orderMapper.toEntities(request.getOrders(), now);
    orderRepository.bulkInsert(orders);

    List<OrderItem> items = new ArrayList<>();
    for (int i = 0; i < request.getOrders().size(); i++) {
      V1CreateOrderRequest.Order reqOrder = request.getOrders().get(i);
      Order order = orders.get(i);

      for (V1CreateOrderRequest.OrderItem reqItem : reqOrder.getOrderItems()) {
        OrderItem item = orderMapper.toEntity(reqItem, order, now);
        items.add(item);
      }
    }

    orderItemRepository.bulkInsert(items);

    List<OrderCreatedMessage> messages = orderMapper.toMessages(orders, items);
    rabbitMqProducer.publish(messages);

    V1CreateOrderResponse response = new V1CreateOrderResponse();
    List<V1CreateOrderResponse.Order> responseOrders = orders.stream()
            .map(order -> {
                List<OrderItem> orderItems = items.stream()
                        .filter(item -> item.getOrder().getId().equals(order.getId()))
                        .collect(Collectors.toList());
                return orderMapper.toCreateResponse(order, orderItems);
            })
            .collect(Collectors.toList());

    response.setOrders(responseOrders);
    return response;
  }

  @Transactional
  public void updateOrdersStatus(V1UpdateOrdersStatusRequest request) throws SQLException {
    if (request.getOrderIds() == null || request.getOrderIds().length == 0) {
      return;
    }

    QueryOrdersDalModel dalModel = new QueryOrdersDalModel();
    Long[] ids = java.util.Arrays.stream(request.getOrderIds()).boxed().toArray(Long[]::new);
    dalModel.setIds(ids);

    List<Order> orders = orderRepository.query(dalModel);

    if (orders.isEmpty()) {
      return;
    }

    List<OrderStatusChangedMessage> messages = new ArrayList<>();

    for (Order order : orders) {
      if ("created".equalsIgnoreCase(order.getStatus()) && "completed".equalsIgnoreCase(request.getNewStatus())) {
        throw new IllegalArgumentException("Invalid status transition from created to completed for order " + order.getId());
      }
      order.setStatus(request.getNewStatus());
      messages.add(new OrderStatusChangedMessage(order.getId(), request.getNewStatus()));
    }

    orderRepository.saveAll(orders);
    rabbitMqProducer.publish(messages);
  }

  @Transactional(readOnly = true)
  public V1QueryOrdersResponse queryOrders(V1QueryOrdersRequest request)
      throws SQLException {
    QueryOrdersDalModel dalModel = new QueryOrdersDalModel();
    dalModel.setIds(request.getIds());
    dalModel.setCustomerIds(request.getCustomerIds());
    dalModel.setLimit(request.getLimit());
    dalModel.setOffset(request.getOffset());

    List<OrderUnit> orderUnits = getOrders(dalModel, true);

    V1QueryOrdersResponse response = new V1QueryOrdersResponse();
    List<V1QueryOrdersResponse.Order> responseOrders = orderUnits.stream()
            .map(orderMapper::toQueryResponse)
            .collect(Collectors.toList());

    response.setOrders(responseOrders);
    return response;
  }

  @Transactional(readOnly = true)
  public List<OrderUnit> getOrders(QueryOrdersDalModel model, boolean includeOrderItems)
      throws SQLException {
    List<Order> orders = orderRepository.query(model);

    if (orders.isEmpty()) {
      return List.of();
    }

    List<OrderItem> items = List.of();
    if (includeOrderItems) {
      Long[] orderIds = orders.stream().map(Order::getId).toArray(Long[]::new);
      QueryOrderItemsDalModel itemModel = new QueryOrderItemsDalModel();
      itemModel.setOrderIds(orderIds);
      itemModel.setLimit(model.getLimit());
      itemModel.setOffset(model.getOffset());
      items = orderItemRepository.query(itemModel);
    }

    return orderMapper.toOrderUnits(orders, items);
  }

}
