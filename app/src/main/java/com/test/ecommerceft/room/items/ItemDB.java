package com.test.ecommerceft.room.items;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Item.class}, version = ItemDB.VERSION)
public abstract class ItemDB extends RoomDatabase {
    static final int VERSION = 3;
    public abstract ItemDao getItemDao();
}

