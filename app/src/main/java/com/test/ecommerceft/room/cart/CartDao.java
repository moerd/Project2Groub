package com.test.ecommerceft.room.cart;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CartDao {
    @Insert
    void insert(Cart... cart);

    @Query("SELECT * FROM Cart")
    List<Cart> getAllItems();
}
