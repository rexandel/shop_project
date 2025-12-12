package org.example.shop.DAL.orders.model;

import lombok.Data;

@Data
public class QueryOrdersDalModel {
    private Long[] ids;
    private Long[] customerIds;
    private int limit;
    private int offset;
}
