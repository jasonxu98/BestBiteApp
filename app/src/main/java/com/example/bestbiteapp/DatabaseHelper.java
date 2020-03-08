package com.example.bestbiteapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "BestBite.db";
    public static final String TBL_NAME = "MENU";
    public static final String COL_1 = "dish";
    public static final String COL_2 = "rating";

    public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, 1);
    }

    public String[] GetTotalMenu(){

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TBL_NAME + " (dish TEXT PRIMARY KEY,rating INTEGER)");
        String[] TotalMenu = GetTotalMenu();
        for (int i = 0; i < TotalMenu.length; i++){
            String DishName = TotalMenu[i];


        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_NAME);
        onCreate((db));
    }
}
