package com.example.bestbiteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "BestBite.db";
    public static final String TBL_NAME = "Menu";
//    public static final String COL_1 = "dish";
//    public static final String COL_2 = "rating";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

//    public ArrayList GetTotalMenu(){
//
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE " + TBL_NAME + " (dish TEXT PRIMARY KEY,rating INTEGER)");
        } catch (SQLiteException e) {
            try {
                throw new IOException(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_NAME);
        onCreate(db);
    }

    public void updateMenu(ArrayList<String> TotalMenu){
        for (int i = 0; i < TotalMenu.size(); i++){
            Object DishName = TotalMenu.get(i);
            String DishStr = String.valueOf(DishName);
            SQLiteDatabase connection = this.getWritableDatabase();
            Cursor cur = connection.rawQuery("SELECT * FROM "+ TBL_NAME + " WHERE dish = ? ", new String[] { DishStr } );
            if (cur.getCount() == 0){
                ContentValues contentValues = new ContentValues();
                contentValues.put("dish", DishStr);
                contentValues.put("rating", 0);
                connection.insert(TBL_NAME, null, contentValues);
            }
            cur.close();
        }
    }

    public int checkRating(String DishStr) {
        SQLiteDatabase connection = this.getReadableDatabase();
        Cursor cur = connection.rawQuery("SELECT rating FROM "+ TBL_NAME + " WHERE dish = ?", new String[] { DishStr } );
        cur.moveToFirst();
        int index = cur.getColumnIndexOrThrow("rating");
        int curr_rating = cur.getInt(index);
        return curr_rating;
    }

    public void updateRating(String DishStr, int newRating){
        SQLiteDatabase connection = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("rating", newRating);
        connection.update(TBL_NAME, cv, "dish = ?", new String[]{DishStr});
    }
}
