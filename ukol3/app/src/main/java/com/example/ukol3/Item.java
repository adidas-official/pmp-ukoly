package com.example.ukol3;

public class Item {
    String name;
    Integer quantity;
    boolean isCrossedOut;

    public Item(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
        this.isCrossedOut = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public boolean isCrossedOut() {
        return isCrossedOut;
    }

    public void setCrossedOut(boolean crossedOut) {
        isCrossedOut = crossedOut;
    }


    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
