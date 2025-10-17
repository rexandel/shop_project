package org.example.shop_project.DAL.orders.model;

public class QueryOrdersDalModel {
    private Long[] ids;
    private Long[] customerIds;
    private int limit;
    private int offset;

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

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
