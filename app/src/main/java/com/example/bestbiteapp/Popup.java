package com.example.bestbiteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class Popup extends AppCompatActivity {

    private DatabaseHelper db;
    private String value;
    private TextView DishName;

    private int curRating = 0;
    Button zero;
    Button two;
    Button four;
    Button eight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_popup);
        db = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            value = extras.getString("key");
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.7));

        WindowManager.LayoutParams Params = getWindow().getAttributes();
        Params.gravity = Gravity.CENTER;

        getWindow().setAttributes(Params);

        DishName = (TextView) findViewById(R.id.DishName);
        DishName.setText(value);

        zero = (Button) findViewById(R.id.zero);

        try {
            curRating = db.checkRating(value);
        } catch (SQLiteException e) {
            try {
                throw new IOException(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        ++curRating;
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zero.setBackgroundColor(Color.parseColor("#f2c649"));
                zero.setText(String.valueOf(curRating));
            }
        });

        two = (Button) findViewById(R.id.two);
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
