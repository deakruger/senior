package com.api.produtosservicos.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ItemDto {
    @NotBlank
    @Size(max = 50)
    private String description;
    @NotNull
    private Double value;
    @NotNull
    private char type;

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Double getValue() {
        return value;
    }
    public void setValue(Double value) {
        this.value = value;
    }
    public char getType() {
        return type;
    }
    public void setType(char type) {
        this.type = type;
    }
}