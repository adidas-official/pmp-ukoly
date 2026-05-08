package com.example.ukol3;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Item.class, ShoppingList.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ShoppingDao shoppingDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            // Changed database name to force recreation and bypass migration issues
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "shopping_db_v2")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
