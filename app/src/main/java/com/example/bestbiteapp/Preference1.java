package com.example.bestbiteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Preference1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference1);
        getSupportActionBar().hide();
    }
}
