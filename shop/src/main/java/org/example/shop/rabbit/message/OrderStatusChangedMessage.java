package org.example.shop.rabbit.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusChangedMessage extends BaseMessage {
    private Long id;
    private String status;

    @Override
    public String getRoutingKey() {
        return "order.status.changed";
    }
}
