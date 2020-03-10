package com.example.bestbiteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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
    Button back;

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


        try {
            curRating = db.checkRating(value);
        } catch (SQLiteException e) {
            try {
                throw new IOException(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        zero = (Button) findViewById(R.id.zero);
        two = (Button) findViewById(R.id.two);
        four = (Button) findViewById(R.id.four);
        eight = (Button) findViewById(R.id.eight);

        if(curRating == 0) zero.setBackgroundColor(Color.parseColor("#f2c649"));
        else if(curRating == 2) two.setBackgroundColor(Color.parseColor("#f2c649"));
        else if(curRating == 4) four.setBackgroundColor(Color.parseColor("#f2c649"));
        else eight.setBackgroundColor(Color.parseColor("#f2c649"));

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curRating == 0) {
                    return;
                }
                zero.setBackgroundColor(Color.parseColor("#f2c649"));
                db.updateRating(value, 0);
                if(curRating == 2) {
                    two.setBackgroundColor(Color.WHITE);
                } else if(curRating == 4) {
                    four.setBackgroundColor(Color.WHITE);
                } else if(curRating == 8) {
                    eight.setBackgroundColor(Color.WHITE);
                }
                curRating = 0;



//                zero.setText(String.valueOf(curRating + 1));
            }
        });


        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curRating == 2) {
                    return;
                }
                two.setBackgroundColor(Color.parseColor("#f2c649"));
                db.updateRating(value, 2);
                if(curRating == 0) {
                    zero.setBackgroundColor(Color.WHITE);
                } else if(curRating == 4) {
                    four.setBackgroundColor(Color.WHITE);
                } else if(curRating == 8) {
                    eight.setBackgroundColor(Color.WHITE);
                }
                curRating = 2;
            }
        });


        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curRating == 4) {
                    return;
                }
                four.setBackgroundColor(Color.parseColor("#f2c649"));
                db.updateRating(value, 4);
                if(curRating == 0) {
                    zero.setBackgroundColor(Color.WHITE);
                } else if(curRating == 2) {
                    two.setBackgroundColor(Color.WHITE);
                } else if(curRating == 8) {
                    eight.setBackgroundColor(Color.WHITE);
                }
                curRating = 4;
            }
        });


        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curRating == 8) {
                    return;
                }
                eight.setBackgroundColor(Color.parseColor("#f2c649"));
                db.updateRating(value, 8);
                if(curRating == 0) {
                    zero.setBackgroundColor(Color.WHITE);
                } else if(curRating == 4) {
                    four.setBackgroundColor(Color.WHITE);
                } else if(curRating == 2) {
                    two.setBackgroundColor(Color.WHITE);
                }
                curRating = 8;
            }
        });

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
