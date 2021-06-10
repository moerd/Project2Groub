package com.test.ecommerceft.room.cart;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public final class CartDao_Impl implements CartDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfCart;

  public CartDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCart = new EntityInsertionAdapter<Cart>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Cart`(`id`,`name`,`variant`,`image`,`price`,`inventory`,`count`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Cart value) {
        stmt.bindLong(1, value.getId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getVariant() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getVariant());
        }
        if (value.getImage() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getImage());
        }
        stmt.bindLong(5, value.getPrice());
        stmt.bindLong(6, value.getInventory());
        stmt.bindLong(7, value.getCount());
      }
    };
  }

  @Override
  public void insert(Cart... cart) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfCart.insert(cart);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Cart> getAllItems() {
    final String _sql = "SELECT * FROM Cart";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfVariant = _cursor.getColumnIndexOrThrow("variant");
      final int _cursorIndexOfImage = _cursor.getColumnIndexOrThrow("image");
      final int _cursorIndexOfPrice = _cursor.getColumnIndexOrThrow("price");
      final int _cursorIndexOfInventory = _cursor.getColumnIndexOrThrow("inventory");
      final int _cursorIndexOfCount = _cursor.getColumnIndexOrThrow("count");
      final List<Cart> _result = new ArrayList<Cart>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Cart _item;
        _item = new Cart();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item.setName(_tmpName);
        final String _tmpVariant;
        _tmpVariant = _cursor.getString(_cursorIndexOfVariant);
        _item.setVariant(_tmpVariant);
        final String _tmpImage;
        _tmpImage = _cursor.getString(_cursorIndexOfImage);
        _item.setImage(_tmpImage);
        final int _tmpPrice;
        _tmpPrice = _cursor.getInt(_cursorIndexOfPrice);
        _item.setPrice(_tmpPrice);
        final int _tmpInventory;
        _tmpInventory = _cursor.getInt(_cursorIndexOfInventory);
        _item.setInventory(_tmpInventory);
        final int _tmpCount;
        _tmpCount = _cursor.getInt(_cursorIndexOfCount);
        _item.setCount(_tmpCount);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
