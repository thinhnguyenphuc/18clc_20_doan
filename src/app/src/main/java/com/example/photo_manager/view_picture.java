package com.example.photo_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class view_picture extends AppCompatActivity {
    ImageView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_picture);

        view =findViewById(R.id.image);

        Intent intent = getIntent();
        view.setImageResource(intent.getIntExtra("image", 0));
    }
}