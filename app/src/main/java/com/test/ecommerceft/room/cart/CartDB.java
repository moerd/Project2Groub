package com.test.ecommerceft.room.cart;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Cart.class}, version = 4)
public abstract class CartDB extends RoomDatabase {
    public abstract CartDao getCartDao();

}
