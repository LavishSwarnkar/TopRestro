package com.lavish.toprestro.old.data;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RestaurantDatabase_Impl extends RestaurantDatabase {
  private volatile RestaurantDao _restaurantDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `restaurants` (`rId` TEXT NOT NULL, `id` TEXT, `name` TEXT, `imageURL` TEXT, `ownerEmail` TEXT, `avgRating` REAL NOT NULL, `noOfRatings` INTEGER NOT NULL, PRIMARY KEY(`rId`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '49f2badd5af80be2ba7e517be5a8cf40')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `restaurants`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsRestaurants = new HashMap<String, TableInfo.Column>(7);
        _columnsRestaurants.put("rId", new TableInfo.Column("rId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("id", new TableInfo.Column("id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("imageURL", new TableInfo.Column("imageURL", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("ownerEmail", new TableInfo.Column("ownerEmail", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("avgRating", new TableInfo.Column("avgRating", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurants.put("noOfRatings", new TableInfo.Column("noOfRatings", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRestaurants = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRestaurants = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRestaurants = new TableInfo("restaurants", _columnsRestaurants, _foreignKeysRestaurants, _indicesRestaurants);
        final TableInfo _existingRestaurants = TableInfo.read(_db, "restaurants");
        if (! _infoRestaurants.equals(_existingRestaurants)) {
          return new RoomOpenHelper.ValidationResult(false, "restaurants(com.lavish.toprestro.old.models.Restaurant).\n"
                  + " Expected:\n" + _infoRestaurants + "\n"
                  + " Found:\n" + _existingRestaurants);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "49f2badd5af80be2ba7e517be5a8cf40", "eab07100cb8dac51a72d71d898f3aa3a");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "restaurants");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `restaurants`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(RestaurantDao.class, RestaurantDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public RestaurantDao restaurantDao() {
    if (_restaurantDao != null) {
      return _restaurantDao;
    } else {
      synchronized(this) {
        if(_restaurantDao == null) {
          _restaurantDao = new RestaurantDao_Impl(this);
        }
        return _restaurantDao;
      }
    }
  }
}
