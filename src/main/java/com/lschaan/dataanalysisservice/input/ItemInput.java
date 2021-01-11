package com.lschaan.dataanalysisservice.input;

public class ItemInput {

    private String id;
    private Long quantity;
    private Double price;

    public ItemInput() {
    }

    public ItemInput(String id, Long quantity, Double price) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
    }

    public Double getItemValue() {
        return this.price * this.quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
