package org.example.shop_project.validators;

import org.springframework.stereotype.Component;

@Component
public class ValidatorFactory {

  private final OrderValidator orderValidator;
  private final V1CreateOrderRequestValidator createOrderRequestValidator;
  private final V1QueryOrdersRequestValidator queryOrdersRequestValidator;

  public ValidatorFactory(
      OrderValidator orderValidator,
      V1CreateOrderRequestValidator createOrderRequestValidator,
      V1QueryOrdersRequestValidator queryOrdersRequestValidator) {
    this.orderValidator = orderValidator;
    this.createOrderRequestValidator = createOrderRequestValidator;
    this.queryOrdersRequestValidator = queryOrdersRequestValidator;
  }

  public OrderValidator getOrderValidator() {
    return orderValidator;
  }

  public V1CreateOrderRequestValidator getCreateOrderRequestValidator() {
    return createOrderRequestValidator;
  }

  public V1QueryOrdersRequestValidator getQueryOrdersRequestValidator() {
    return queryOrdersRequestValidator;
  }
}
