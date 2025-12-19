package org.example.shop.DAL.orders.model;

import lombok.Data;

@Data
public class QueryOrderItemsDalModel {
    private Long[] ids;
    private Long[] orderIds;
    private int limit;
    private int offset;
}

