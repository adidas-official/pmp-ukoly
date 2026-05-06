package com.example.ukol3;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shopping_lists")
public class ShoppingList {
    @PrimaryKey(autoGenerate = true)
    private int listId;
    private String name;

    public ShoppingList(String name) {
        this.name = name;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
