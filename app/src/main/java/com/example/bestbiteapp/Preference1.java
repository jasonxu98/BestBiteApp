package com.example.bestbiteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Preference1 extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ArrayList<String> options;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference1);
        getSupportActionBar().hide();

        ArrayList<String> dishes = new ArrayList<String >();
        Downloader d = new Downloader();
        d.execute("http://umtri.org/BestBiteServer/totalmenu/Menu.txt");
        try {
            dishes = d.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        // Creating ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dishes);
        // Specify layout to be used when list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applying the adapter to our spinner
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(this);

        options = dishes;

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

        Toast.makeText(this, " You select >> "+options.get(position), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
