package controllers;

import BLL.models.OrderUnit;
import BLL.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Tag(name = "OrderController", description = "CRUD операции над контактами")
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "загрузить все заказы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список заказов",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderUnit.class))))
    })
    @PostMapping("/bulk")
    public ResponseEntity<List<OrderUnit>> bulkInsert(@RequestBody List<OrderUnit> orders) throws SQLException {
        List<OrderUnit> result = orderService.insertOrdersBulk(orders);
        return ResponseEntity.ok(result);
    }
}

