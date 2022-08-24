package com.api.produtosservicos.dtos;

import java.sql.Timestamp;
import java.util.UUID;
import javax.validation.constraints.NotNull;

public class OrderDto {
    private UUID order;
    private Integer number;
    private Timestamp date;
    @NotNull
    private Double percentualDiscount;
    private Double totalValue;

    public UUID getOrder() {
        return order;
    }

    public void setOrder(UUID order) {
        this.order = order;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Double getPercentualDiscount() {
        return percentualDiscount;
    }

    public void setPercentualDiscount(Double percentualDiscount) {
        this.percentualDiscount = percentualDiscount;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

}
