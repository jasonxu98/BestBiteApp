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

            ArrayList<String> TotalMenu = new ArrayList<String>();
            TotalMenu.add("chicken");
            TotalMenu.add("steak");
            TotalMenu.add("pork");
            TotalMenu.add("curry");

            for (int i = 0; i < TotalMenu.size(); i++){
                String DishName = TotalMenu.get(i);
                SQLiteDatabase connection_r = this.getReadableDatabase();
                Cursor cur = db.rawQuery("SELECT * FROM "+ TBL_NAME + " WHERE dish = " + DishName, null );
                if (cur.getCount() == 0){
                    SQLiteDatabase connection_w = this.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("dish", DishName);
                    contentValues.put("rating", 0);
                    connection_w.insert(TBL_NAME, null, contentValues);
                }
            }
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
        onCreate((db));
    }

    public int checkRating(String DishName) {
        SQLiteDatabase connection_r = this.getReadableDatabase();
        Cursor cur = connection_r.rawQuery("SELECT rating FROM "+ TBL_NAME + " WHERE dish = " + DishName, null );
        cur.moveToFirst();
        return cur.getInt(cur.getPosition());
    }

    public void updateRating(String DishName, int newRating){
        SQLiteDatabase connection_w = this.getWritableDatabase();
        connection_w.execSQL("UPDATE TABLE " + TBL_NAME + " SET rating = " + newRating + " WHERE dish = " + DishName );
    }
}
