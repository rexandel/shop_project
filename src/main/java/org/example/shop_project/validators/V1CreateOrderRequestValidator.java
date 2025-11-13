package org.example.shop_project.validators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.shop_project.DTO.requests.V1CreateOrderRequest;
import org.springframework.stereotype.Component;

@Component
public class V1CreateOrderRequestValidator {

  private final OrderValidator orderValidator;

  public V1CreateOrderRequestValidator(OrderValidator orderValidator) {
    this.orderValidator = orderValidator;
  }

  public Map<String, List<String>> validate(V1CreateOrderRequest request) {
    Map<String, List<String>> errors = new HashMap<>();

    if (request.getOrders() == null || request.getOrders().isEmpty()) {
      errors.put("Orders", List.of("Orders cannot be null or empty"));
      return errors;
    }

    for (int i = 0; i < request.getOrders().size(); i++) {
      V1CreateOrderRequest.Order order = request.getOrders().get(i);
      String orderPrefix = "Orders[" + i + "]";

      try {
        orderValidator.validateOrder(order);
      } catch (IllegalArgumentException e) {
        errors.put(orderPrefix, List.of(e.getMessage()));
      }
    }

    return errors;
  }
}

