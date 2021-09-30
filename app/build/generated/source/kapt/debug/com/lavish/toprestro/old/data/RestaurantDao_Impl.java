package com.lavish.toprestro.old.data;

import android.database.Cursor;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.lavish.toprestro.old.models.Restaurant;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RestaurantDao_Impl implements RestaurantDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Restaurant> __insertionAdapterOfRestaurant;

  public RestaurantDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRestaurant = new EntityInsertionAdapter<Restaurant>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `restaurants` (`rId`,`id`,`name`,`imageURL`,`ownerEmail`,`avgRating`,`noOfRatings`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Restaurant value) {
        if (value.getRId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getRId());
        }
        if (value.getId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getId());
        }
        if (value.getName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getName());
        }
        if (value.getImageURL() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getImageURL());
        }
        if (value.getOwnerEmail() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getOwnerEmail());
        }
        stmt.bindDouble(6, value.getAvgRating());
        stmt.bindLong(7, value.getNoOfRatings());
      }
    };
  }

  @Override
  public Object insertRestaurant(final Restaurant restaurant,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRestaurant.insert(restaurant);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object insertAllRestaurants(final List<Restaurant> restaurants,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRestaurant.insert(restaurants);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<Restaurant>> getRestaurants() {
    final String _sql = "SELECT * FROM restaurants";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"restaurants"}, new Callable<List<Restaurant>>() {
      @Override
      public List<Restaurant> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRId = CursorUtil.getColumnIndexOrThrow(_cursor, "rId");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfImageURL = CursorUtil.getColumnIndexOrThrow(_cursor, "imageURL");
          final int _cursorIndexOfOwnerEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerEmail");
          final int _cursorIndexOfAvgRating = CursorUtil.getColumnIndexOrThrow(_cursor, "avgRating");
          final int _cursorIndexOfNoOfRatings = CursorUtil.getColumnIndexOrThrow(_cursor, "noOfRatings");
          final List<Restaurant> _result = new ArrayList<Restaurant>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Restaurant _item;
            final String _tmpRId;
            if (_cursor.isNull(_cursorIndexOfRId)) {
              _tmpRId = null;
            } else {
              _tmpRId = _cursor.getString(_cursorIndexOfRId);
            }
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpImageURL;
            if (_cursor.isNull(_cursorIndexOfImageURL)) {
              _tmpImageURL = null;
            } else {
              _tmpImageURL = _cursor.getString(_cursorIndexOfImageURL);
            }
            final String _tmpOwnerEmail;
            if (_cursor.isNull(_cursorIndexOfOwnerEmail)) {
              _tmpOwnerEmail = null;
            } else {
              _tmpOwnerEmail = _cursor.getString(_cursorIndexOfOwnerEmail);
            }
            final float _tmpAvgRating;
            _tmpAvgRating = _cursor.getFloat(_cursorIndexOfAvgRating);
            final int _tmpNoOfRatings;
            _tmpNoOfRatings = _cursor.getInt(_cursorIndexOfNoOfRatings);
            _item = new Restaurant(_tmpRId,_tmpId,_tmpName,_tmpImageURL,_tmpOwnerEmail,_tmpAvgRating,_tmpNoOfRatings);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
