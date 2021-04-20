package com.example.photo_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;




public class Photo_Details extends AppCompatActivity {

    private Picture_Model picture_model = new Picture_Model(null,null,0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo__details);

        Intent receiver = getIntent();
        String tmp = receiver.getStringExtra("uri");

        picture_model.setUri(Uri.parse(tmp));
        picture_model.setName(receiver.getStringExtra("name"));
        picture_model.setSize(receiver.getIntExtra("size",0));


        ImageView imageView_detail = (ImageView)findViewById(R.id.imageView_details);
        TextView textView_path = (TextView)findViewById(R.id.textView_path);
        TextView textView_resolution = (TextView)findViewById(R.id.textView_resolution);

        Glide.with(this).load(picture_model.getUri()).into(imageView_detail);



        textView_path.setText(picture_model.getUri().getPath());
    }
}