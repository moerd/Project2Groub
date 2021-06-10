package com.test.ecommerceft.room.items;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    void insert(Item... item);

    @Query("SELECT * FROM Item")
    List<Item> getAllItems();
}
