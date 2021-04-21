package com.example.photo_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.photo_manager.PEAdapters.Utility;

public class ViewPhoto extends AppCompatActivity {
    private Picture_Model picture_model = new Picture_Model(null,null,null,0);
    SubsamplingScaleImageView imageView;
    ImageButton favourite_button, edit_button, share_button, delete_button;
    Toolbar top_toolbar, bottom_toolbar;
    boolean favourite_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);


        Intent receiver = getIntent();
        String tmp = receiver.getStringExtra("uri");

        picture_model.setUri(Uri.parse(tmp));
        picture_model.setName(receiver.getStringExtra("name"));
        picture_model.setTime(receiver.getStringExtra("time"));
        picture_model.setSize(receiver.getIntExtra("size",0));

        imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
        imageView.setImage(ImageSource.uri(picture_model.getUri()));

        //Glide.with(this).load(picture_model.getUri()).into(imageView);


        top_toolbar = findViewById(R.id.toolbar_top);
        bottom_toolbar = findViewById(R.id.toolbar_bottom);

        favourite_button = findViewById(R.id.favourite_button);
        favourite_flag = Utility.checkImageIsFavourite("");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (top_toolbar.getVisibility() == View.INVISIBLE) {
                    top_toolbar.setVisibility(View.VISIBLE);
                } else {
                    top_toolbar.setVisibility(View.INVISIBLE);
                }

                if (bottom_toolbar.getVisibility() == View.INVISIBLE) {
                    bottom_toolbar.setVisibility(View.VISIBLE);
                } else {
                    bottom_toolbar.setVisibility(View.INVISIBLE);
                }
            }
        });

        this.favourite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!favourite_flag) {
                    favourite_button.setImageResource(R.drawable.red_heart_icon);
                    favourite_flag = true;
                } else {
                    favourite_button.setImageResource(R.drawable.favourite_icon);
                    favourite_flag = false;
                }
            }
        });

        this.edit_button = findViewById(R.id.edit_button);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPhoto.this, PhotoEditActivity.class);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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