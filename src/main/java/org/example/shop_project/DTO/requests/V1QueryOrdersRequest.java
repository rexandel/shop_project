package org.example.shop_project.DTO.requests;

import jakarta.validation.constraints.Min;

public class V1QueryOrdersRequest {
    
    private Long[] ids;
    private Long[] customerIds;
    
    @Min(value = 0, message = "Limit must be non-negative")
    private Integer limit = 10;
    
    @Min(value = 0, message = "Offset must be non-negative")
    private Integer offset = 0;

    public Long[] getIds() {
        return ids;
    }

    public void setIds(Long[] ids) {
        this.ids = ids;
    }

    public Long[] getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(Long[] customerIds) {
        this.customerIds = customerIds;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
