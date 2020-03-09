package com.example.bestbiteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Preference1 extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String[] options;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference1);
        getSupportActionBar().hide();
        db = new DatabaseHelper(this);
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        // Creating ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Animals, android.R.layout.simple_spinner_item);
        // Specify layout to be used when list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applying the adapter to our spinner
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(this);

        options = Preference1.this.getResources().getStringArray(R.array.Animals);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected = options[position];
        Toast.makeText(this, selected + "'s current rating: " + db.checkRating(selected), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), Popup.class);
        startActivity(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
