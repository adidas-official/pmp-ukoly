package com.example.ukol3;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shopping_lists")
public class ShoppingList {
    @PrimaryKey(autoGenerate = true)
    private int listId;
    private String name;
    private int backgroundColor = 0xFFFFFFFF;
    private int textColor = 0xFF000000;

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

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

}
