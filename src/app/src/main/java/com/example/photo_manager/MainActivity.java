package com.example.photo_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button activity2,activity3;
    Button album_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity2 = (Button) findViewById(R.id.button_activity2);

        activity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Photo_Details.class);
                startActivity(intent);
            }
        });

        activity3 = (Button) findViewById(R.id.button_activity3);

        activity3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, View_By_Date.class);
                startActivity(intent);
            }
        });

        album_detail = findViewById(R.id.album_detail);

        album_detail.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, album_detail.class);
            startActivity(intent);
        });
    }
}