package com.example.communityhub.legacyui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "communityswap.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_ITEMS = "items";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_CATEGORY = "category";
    private static final String COL_DESC = "description";
    private static final String COL_IMAGE = "imageUri";
    private static final String COL_FAV = "favorite";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + TABLE_ITEMS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_CATEGORY + " TEXT, " +
                COL_DESC + " TEXT, " +
                COL_IMAGE + " TEXT, " +
                COL_FAV + " INTEGER DEFAULT 0)";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    public long addItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, item.getTitle());
        cv.put(COL_CATEGORY, item.getCategory());
        cv.put(COL_DESC, item.getDescription());
        cv.put(COL_IMAGE, item.getImageUri());
        cv.put(COL_FAV, item.isFavorite() ? 1 : 0);
        long id = db.insert(TABLE_ITEMS, null, cv);
        db.close();
        return id;
    }

    public List<Item> getAllItems() {
        List<Item> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_ITEMS, null, null, null, null, null, COL_ID + " DESC");
        if (c != null) {
            while (c.moveToNext()) {
                Item it = new Item();
                it.setId(c.getLong(c.getColumnIndexOrThrow(COL_ID)));
                it.setTitle(c.getString(c.getColumnIndexOrThrow(COL_TITLE)));
                it.setCategory(c.getString(c.getColumnIndexOrThrow(COL_CATEGORY)));
                it.setDescription(c.getString(c.getColumnIndexOrThrow(COL_DESC)));
                it.setImageUri(c.getString(c.getColumnIndexOrThrow(COL_IMAGE)));
                it.setFavorite(c.getInt(c.getColumnIndexOrThrow(COL_FAV)) == 1);
                list.add(it);
            }
            c.close();
        }
        db.close();
        return list;
    }

    public void updateFavorite(long id, boolean fav) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_FAV, fav ? 1 : 0);
        db.update(TABLE_ITEMS, cv, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void clearAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_ITEMS, null, null);
        db.close();
    }
}
