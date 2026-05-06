package com.example.ukol3;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class Item {
    @PrimaryKey(autoGenerate = true)
    Integer id;
    Integer listId;
    String name;
    Integer quantity;
    boolean isCrossedOut;

    public Item(Integer listId, String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
        this.isCrossedOut = false;
        this.listId = listId;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getListId() {
        return listId;
    }

    public void setListId(Integer listId) {
        this.listId = listId;
    }
}
