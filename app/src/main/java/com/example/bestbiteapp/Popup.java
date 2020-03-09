package com.example.bestbiteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Popup extends AppCompatActivity {

    private DatabaseHelper db;
    private String value;
    private TextView DishName;
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
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
