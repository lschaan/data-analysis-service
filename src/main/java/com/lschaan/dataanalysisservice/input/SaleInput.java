package com.lschaan.dataanalysisservice.input;

import java.util.List;

public class SaleInput implements DataInput {

    private String id;
    private List<ItemInput> items;
    private String salesman;

    public Double getTotalValue() {
        return this.items.stream()
                .mapToDouble(ItemInput::getItemValue)
                .sum();
    }

    public boolean isFromSalesman(String salesman) {
        return this.salesman.equals(salesman);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ItemInput> getItems() {
        return items;
    }

    public void setItems(List<ItemInput> items) {
        this.items = items;
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman;
    }
}
