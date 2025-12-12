package org.example.shop_project.DAL.orders.model;

public class QueryOrderItemsDalModel {
    private Long[] ids;
    private Long[] orderIds;
    private int limit;
    private int offset;

    public Long[] getIds() {
        return ids;
    }

    public void setIds(Long[] ids) {
        this.ids = ids;
    }

    public Long[] getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(Long[] orderIds) {
        this.orderIds = orderIds;
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

