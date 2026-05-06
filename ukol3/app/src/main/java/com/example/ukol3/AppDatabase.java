package com.example.ukol3;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Item.class, ShoppingList.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ShoppingDao shoppingDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "shopping_database")
                    .allowMainThreadQueries() // Povolí dotazy na hlavním vlákně (vhodné pro malé školní projekty)
                    .build();
        }
        return instance;
    }
}