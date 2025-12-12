package org.example.shop.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidatorFactory {

  private final OrderValidator orderValidator;
  private final V1CreateOrderRequestValidator createOrderRequestValidator;
  private final V1QueryOrdersRequestValidator queryOrdersRequestValidator;

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
