package org.example.shop.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.shop.BLL.orders.service.OrderService;
import org.example.shop.DTO.requests.V1CreateOrderRequest;
import org.example.shop.DTO.requests.V1UpdateOrdersStatusRequest;
import org.example.shop.DTO.responses.V1CreateOrderResponse;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderGenerator implements CommandLineRunner {

    private final OrderService orderService;
    private final Random random = new Random();

    @Override
    public void run(String... args) {
        new Thread(this::generateOrdersLoop).start();
    }

    private void generateOrdersLoop() {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .seed(123L)
                .objectPoolSize(100)
                .randomizationDepth(3)
                .stringLengthRange(5, 25)
                .collectionSizeRange(1, 3)
                .scanClasspathForConcreteTypes(true)
                .overrideDefaultInitialization(false)
                .ignoreRandomizationErrors(true);

        EasyRandom easyRandom = new EasyRandom(parameters);

        log.info("Starting background order generation...");

        while (true) {
            try {
                List<V1CreateOrderRequest.Order> orders = IntStream.range(0, 50)
                        .mapToObj(i -> {
                            V1CreateOrderRequest.OrderItem orderItem = easyRandom.nextObject(V1CreateOrderRequest.OrderItem.class);
                            orderItem.setPriceCurrency("RUB");
                            orderItem.setPriceCents(100L);
                            orderItem.setQuantity(1);
                            orderItem.setProductId((long) (i + 1));
                            orderItem.setProductTitle("Product " + i);
                            
                            V1CreateOrderRequest.Order order = easyRandom.nextObject(V1CreateOrderRequest.Order.class);
                            order.setTotalPriceCurrency("RUB");
                            order.setTotalPriceCents(1000L);
                            order.setDeliveryAddress("Test Address " + i);
                            order.setCustomerId((long) (i + 1));
                            order.setOrderItems(List.of(orderItem));
                            
                            return order;
                        })
                        .collect(Collectors.toList());

                V1CreateOrderRequest request = new V1CreateOrderRequest();
                request.setOrders(orders);

                log.info("Sending batch of {} orders...", orders.size());
                V1CreateOrderResponse response = orderService.createOrders(request);
                log.info("Batch sent successfully.");

                // Update status for some orders
                if (response.getOrders() != null && !response.getOrders().isEmpty()) {
                    List<Long> orderIds = response.getOrders().stream()
                            .map(V1CreateOrderResponse.Order::getId)
                            .collect(Collectors.toList());
                    
                    // Select random subset of orders to update
                    int updateCount = random.nextInt(orderIds.size()) + 1;
                    long[] idsToUpdate = orderIds.stream()
                            .limit(updateCount)
                            .mapToLong(Long::longValue)
                            .toArray();

                    V1UpdateOrdersStatusRequest updateRequest = new V1UpdateOrdersStatusRequest();
                    updateRequest.setOrderIds(idsToUpdate);
                    updateRequest.setNewStatus("in_progress");

                    log.info("Updating status for {} orders...", idsToUpdate.length);
                    orderService.updateOrdersStatus(updateRequest);
                    log.info("Status update sent successfully.");
                }
                
                Thread.sleep(250);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Error in order generation loop", e);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}
