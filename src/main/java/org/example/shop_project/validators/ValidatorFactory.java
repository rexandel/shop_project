package org.example.shop_project.validators;

import org.springframework.stereotype.Component;

@Component
public class ValidatorFactory {
    
    private final OrderValidator orderValidator;

    public ValidatorFactory(OrderValidator orderValidator) {
        this.orderValidator = orderValidator;
    }

    public OrderValidator getOrderValidator() {
        return orderValidator;
    }
}
