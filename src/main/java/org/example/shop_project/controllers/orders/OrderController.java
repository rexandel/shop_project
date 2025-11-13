package org.example.shop_project.controllers.orders;

import org.example.shop_project.DTO.requests.V1CreateOrderRequest;
import org.example.shop_project.DTO.requests.V1QueryOrdersRequest;
import org.example.shop_project.DTO.responses.V1CreateOrderResponse;
import org.example.shop_project.DTO.responses.V1QueryOrdersResponse;
import org.example.shop_project.BLL.orders.service.OrderService;
import org.example.shop_project.validators.ValidatorFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "OrderController", description = "CRUD операции над заказами")
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final ValidatorFactory validatorFactory;

    public OrderController(OrderService orderService, ValidatorFactory validatorFactory) {
        this.orderService = orderService;
        this.validatorFactory = validatorFactory;
    }

    @Operation(summary = "Создать заказы (batch-create)")
    @PostMapping("/batch-create")
    public ResponseEntity<?> v1BatchCreate(@Valid @RequestBody V1CreateOrderRequest request) {
        try {
            // Дополнительная валидация бизнес-правил
            for (V1CreateOrderRequest.Order order : request.getOrders()) {
                validatorFactory.getOrderValidator().validateOrder(order);
            }

            V1CreateOrderResponse response = orderService.createOrders(request);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Internal server error"));
        }
    }

    @Operation(summary = "Запросить заказы с фильтрацией и пагинацией")
    @PostMapping("/query")
    public ResponseEntity<?> v1QueryOrders(@Valid @RequestBody V1QueryOrdersRequest request) {
        try {
            V1QueryOrdersResponse response = orderService.queryOrders(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Internal server error"));
        }
    }
}

