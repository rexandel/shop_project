package org.example.shop_project.controllers.orders;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;
import org.example.shop_project.BLL.orders.service.OrderService;
import org.example.shop_project.DTO.requests.V1CreateOrderRequest;
import org.example.shop_project.DTO.requests.V1QueryOrdersRequest;
import org.example.shop_project.DTO.responses.V1CreateOrderResponse;
import org.example.shop_project.DTO.responses.V1QueryOrdersResponse;
import org.example.shop_project.validators.ValidatorFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OrderController", description = "CRUD операции над заказами")
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;
  private final ValidatorFactory validatorFactory;

  @Operation(summary = "Создать заказы (batch-create)")
  @PostMapping("/batch-create")
  public ResponseEntity<?> v1BatchCreate(@Valid @RequestBody V1CreateOrderRequest request) {
    Map<String, List<String>> validationErrors =
        validatorFactory.getCreateOrderRequestValidator().validate(request);

    if (!validationErrors.isEmpty()) {
      return ResponseEntity.badRequest().body(validationErrors);
    }

    try {
      V1CreateOrderResponse response = orderService.createOrders(request);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.internalServerError()
          .body(Map.of("error", "Internal server error"));
    }
  }

  @Operation(summary = "Запросить заказы с фильтрацией и пагинацией")
  @PostMapping("/query")
  public ResponseEntity<?> v1QueryOrders(@Valid @RequestBody V1QueryOrdersRequest request) {
    Map<String, List<String>> validationErrors =
        validatorFactory.getQueryOrdersRequestValidator().validate(request);

    if (!validationErrors.isEmpty()) {
      return ResponseEntity.badRequest().body(validationErrors);
    }

    try {
      V1QueryOrdersResponse response = orderService.queryOrders(request);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.internalServerError()
          .body(Map.of("error", "Internal server error"));
    }
  }
}

