package com.example.ukol3;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface ShoppingDao {

    @Query("SELECT * FROM shopping_lists")
    List<ShoppingList> getAllShoppingLists();

    @Insert
    long insertShoppingList(ShoppingList shoppingList);

    @Update
    void updateShoppingList(ShoppingList shoppingList);

    @Delete
    void deleteShoppingList(ShoppingList shoppingList);

    @Query("SELECT * FROM items WHERE listId = :listId")
    List<Item> getItemsForList(int listId);

    @Insert
    long insertItem(Item item);

    @Update
    void updateItem(Item item);

    @Delete
    void deleteItem(Item item);
}
