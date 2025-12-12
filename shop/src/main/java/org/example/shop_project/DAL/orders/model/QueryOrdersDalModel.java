package org.example.shop_project.DAL.orders.model;

import lombok.Data;

@Data
public class QueryOrdersDalModel {
    private Long[] ids;
    private Long[] customerIds;
    private int limit;
    private int offset;
}
