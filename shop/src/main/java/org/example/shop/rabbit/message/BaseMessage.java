package org.example.shop.rabbit.message;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class BaseMessage {
    @JsonIgnore
    public abstract String getRoutingKey();
}
