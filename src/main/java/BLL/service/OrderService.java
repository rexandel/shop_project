package BLL.service;

import BLL.models.OrderItemUnit;
import BLL.models.OrderUnit;
import DAL.entity.Order;
import DAL.entity.OrderItem;
import DAL.repository.jpa.IOrderItemRepository;
import DAL.repository.jpa.IOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final IOrderRepository orderRepository;
    private final IOrderItemRepository orderItemRepository;

    public OrderService(IOrderRepository orderRepository, IOrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public List<OrderUnit> insertOrdersBulk(List<OrderUnit> units) throws SQLException {
        OffsetDateTime now = OffsetDateTime.now();

        // 1. Преобразуем в Entity
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

        // 5. Возвращаем DTO
        return orders.stream().map(o -> {
            OrderUnit ou = new OrderUnit();
            ou.setId(o.getId());
            ou.setCustomerId(o.getCustomerId());
            ou.setDeliveryAddress(o.getDeliveryAddress());
            ou.setTotalPriceCents(o.getTotalPriceCents());
            ou.setTotalPriceCurrency(o.getTotalPriceCurrency());
            ou.setCreatedAt(o.getCreatedAt());
            ou.setUpdatedAt(o.getUpdatedAt());
            ou.setOrderItems(
                    items.stream().filter(it -> it.getOrder().getId().equals(o.getId())).map(it -> {
                        OrderItemUnit iu = new OrderItemUnit();
                        iu.setId(it.getId());
                        iu.setOrderId(o.getId());
                        iu.setProductId(it.getProductId());
                        iu.setQuantity(it.getQuantity());
                        iu.setProductTitle(it.getProductTitle());
                        iu.setProductUrl(it.getProductUrl());
                        iu.setPriceCents(it.getPriceCents());
                        iu.setPriceCurrency(it.getPriceCurrency());
                        iu.setCreatedAt(it.getCreatedAt());
                        iu.setUpdatedAt(it.getUpdatedAt());
                        return iu;
                    }).toList()
            );
            return ou;
        }).toList();
    }
}
