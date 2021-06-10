package com.test.ecommerceft.room.items;

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
public final class ItemDao_Impl implements ItemDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfItem;

  public ItemDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfItem = new EntityInsertionAdapter<Item>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Item`(`SNo`,`name`,`variant`,`image`,`price`,`inventory`,`count`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Item value) {
        stmt.bindLong(1, value.getSNo());
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
  public void insert(Item... item) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfItem.insert(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Item> getAllItems() {
    final String _sql = "SELECT * FROM Item";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfSNo = _cursor.getColumnIndexOrThrow("SNo");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfVariant = _cursor.getColumnIndexOrThrow("variant");
      final int _cursorIndexOfImage = _cursor.getColumnIndexOrThrow("image");
      final int _cursorIndexOfPrice = _cursor.getColumnIndexOrThrow("price");
      final int _cursorIndexOfInventory = _cursor.getColumnIndexOrThrow("inventory");
      final int _cursorIndexOfCount = _cursor.getColumnIndexOrThrow("count");
      final List<Item> _result = new ArrayList<Item>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Item _item;
        _item = new Item();
        final int _tmpSNo;
        _tmpSNo = _cursor.getInt(_cursorIndexOfSNo);
        _item.setSNo(_tmpSNo);
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
