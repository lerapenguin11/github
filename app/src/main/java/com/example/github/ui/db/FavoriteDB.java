package com.example.github.ui.db;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLDataException;

public class FavoriteDB extends SQLiteOpenHelper {
    private static int DB_VERSION = 1;
    private static String DATABASE_NAME = "Repository";
    private static String TABLE_NAME = "FavoriteList";
    public static String KEY_ID = "id";
    public static String ITEM_TITLE = "title";
    public static String ITEM_DISCRIPTION = "discription";
    public static String ITEM_UPDATE = "time_of_change";
    public static String ITEM_STARRED = "starred";
    public static String FAVORITE_STATUS = "favStatus";
    public static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " TEXT," + ITEM_TITLE + " TEXT," + ITEM_DISCRIPTION +
            " TEXT," + ITEM_UPDATE + " TEXT," + ITEM_STARRED + " TEXT," +
            FAVORITE_STATUS + " TEXT)";

    public FavoriteDB(Context context){super(context, DATABASE_NAME, null, DB_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w(TAG, "Upgrading database from version " + i + " to "
                + i1 + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tasks");
        onCreate(sqLiteDatabase);

    }

    public void insertEmpty(){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for(int x = 1; x < 5; x++){
            contentValues.put(KEY_ID, x);
            contentValues.put(FAVORITE_STATUS, "0");

            database.insert(TABLE_NAME, null, contentValues);
        }
    }

    public void insertIntoDatabase(String item_title, String item_description,
                                   String item_update, String id, String item_starred, String fav_status){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ITEM_TITLE, item_title);
        contentValues.put(ITEM_DISCRIPTION, item_description);
        contentValues.put(ITEM_UPDATE, item_update);
        contentValues.put(KEY_ID, id);
        contentValues.put(ITEM_STARRED, item_starred);
        contentValues.put(FAVORITE_STATUS, fav_status);

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        Log.d("FavoritesDB status", item_title + ", favStatus - " + fav_status
                + "-, " + contentValues);
    }

    public Cursor readData(String id){
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "select * from " + TABLE_NAME + " where " + KEY_ID + "=" + id + "";

        return database.rawQuery(sql, null, null);
    }

    public void removeFav(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME + " SET " + FAVORITE_STATUS + " ='0' WHERE " + KEY_ID
                + "=" +id + "";
        sqLiteDatabase.execSQL(sql);
        Log.d("remove", id.toString());
    }

    public Cursor selectAllFavoriteList(){
        SQLiteDatabase database = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + FAVORITE_STATUS + " ='1'";

        return database.rawQuery(sql, null, null);
    }

    public void deleteAllRow() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        db.close();
    }
}
