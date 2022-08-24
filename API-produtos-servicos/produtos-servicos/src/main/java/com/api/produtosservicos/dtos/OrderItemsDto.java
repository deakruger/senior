package com.api.produtosservicos.dtos;

import java.util.UUID;
import javax.validation.constraints.NotNull;

public class OrderItemsDto {
    private UUID order;
    @NotNull
    private UUID itemId;
    @NotNull
    private Double quantity;
    private Double totalValue;

    public UUID getOrderId() {
        return order;
    }

    public void setOrderId(UUID orderId) {
        this.order = orderId;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

}