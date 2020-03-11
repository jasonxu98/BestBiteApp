package com.example.bestbiteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Preference1 extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ArrayList<String> options;
    private DatabaseHelper db;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference1);
        getSupportActionBar().hide();
        db = new DatabaseHelper(this);
        ArrayList<String> dishes = new ArrayList<String >();
        Downloader d = new Downloader();
        d.execute("http://umtri.org/BestBiteServer/totalmenu/Menu.txt");
        try {
            dishes = d.get();
            db.updateMenu(dishes);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dishes.add(0, "Click here to select a dish!");

        SearchableSpinner spinner1 = (SearchableSpinner) findViewById(R.id.spinner1);
        // Creating ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, dishes);
        // Specify layout to be used when list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applying the adapter to our spinner
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(this);

        options = dishes;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage("Are you sure to reset all your preferences?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                db.reset();
                Toast.makeText(getApplicationContext(), "All preferences reset to 0!", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        final AlertDialog alert = builder.create();

        button = (Button) findViewById(R.id.button36);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.show();
            }
        });
    }


    class Downloader extends AsyncTask<String, Void, ArrayList<String> > {
        public ArrayList<String> doInBackground(String... urls) {
            ArrayList<String>  result = new ArrayList<String>();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result.add(line);
                }
                conn.disconnect();
            } catch (Exception e) {
                Log.e("error fetching contents", e.toString());
            }

            return result;
        }
//
//        protected void onPostExecute (ArrayList<String> result) {
//            src.setText(result.get(0));
//        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            return;
        }
        String selected = options.get(position);
        Toast.makeText(this, " You select >> "+options.get(position), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), Popup.class);
        i.putExtra("key", selected);
        startActivity(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
