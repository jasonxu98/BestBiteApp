package com.example.bestbiteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Ranking1 extends AppCompatActivity {
    private TextView src;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking1);
        getSupportActionBar().hide();

        src = (TextView) findViewById(R.id.internetbox1);
    }

    public void fetch(View v) {
        Downloader d = new Downloader();
        d.execute("http://umtri.org/BestBiteServer/totalmenu/Menu.txt");
    }

    class Downloader extends AsyncTask<String, Void, String> {
        public String doInBackground(String... urls) {
            String result = null;
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result = result + line;
                }
                conn.disconnect();
            } catch (Exception e) {

            }

            return result;
        }

        protected void onPostExecute (String result) {
            src.setText("Something is terribly wrong with this");
        }
    }
}