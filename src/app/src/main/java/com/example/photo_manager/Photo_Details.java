package com.example.photo_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;




public class Photo_Details extends AppCompatActivity {

    private Picture_Model picture_model = new Picture_Model(null,null,null,0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo__details);

        Intent receiver = getIntent();
        String tmp = receiver.getStringExtra("uri");

        picture_model.setUri(Uri.parse(tmp));
        picture_model.setName(receiver.getStringExtra("name"));
        picture_model.setTime(receiver.getStringExtra("time"));
        picture_model.setSize(receiver.getIntExtra("size",0));


        ImageView imageView_detail = (ImageView)findViewById(R.id.imageView_details);
        TextView textView_path = (TextView)findViewById(R.id.textView_path);
        TextView textView_resolution = (TextView)findViewById(R.id.textView_resolution);
        TextView textView_size = (TextView)findViewById(R.id.textView_size);
        TextView textView_time = (TextView)findViewById(R.id.textView_time);

        textView_size.setText(String.valueOf(picture_model.getSize()/1024)+"KB");

        Glide.with(this).load(picture_model.getUri()).into(imageView_detail);

        textView_time.setText(picture_model.getTime());

        textView_path.setText(getPath(picture_model.getUri()));

    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}