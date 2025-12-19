package org.example.shop.DTO.requests;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class V1UpdateOrdersStatusRequest {
    private long[] orderIds;
    private String newStatus;
}
