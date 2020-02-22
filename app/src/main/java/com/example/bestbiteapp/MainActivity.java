package com.example.bestbiteapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button1);
    // Example of a call to a native method
//    imageView = (ImageView) findViewById(R.id.logo1);

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button button2 = (Button) findViewById(R.id.button1);
            button2.setText("Yuxuan is GAY!!");
        }
    });

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
}
