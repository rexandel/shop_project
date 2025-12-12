package org.example.shop_project.DTO.requests;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class V1QueryOrdersRequest {
    
    private Long[] ids;
    private Long[] customerIds;
    
    @Min(value = 0, message = "Limit must be non-negative")
    private Integer limit = 10;
    
    @Min(value = 0, message = "Offset must be non-negative")
    private Integer offset = 0;
}
