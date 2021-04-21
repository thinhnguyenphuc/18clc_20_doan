package com.example.photo_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public class ViewPhoto extends AppCompatActivity {
    SubsamplingScaleImageView imageView;
    ImageButton favourite_button, edit_button, share_button, delete_button;
    Toolbar top_toolbar, bottom_toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);
        imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);

        imageView.setImage(ImageSource.resource(R.drawable.photo));

        top_toolbar = findViewById(R.id.toolbar_top);
        bottom_toolbar = findViewById(R.id.toolbar_bottom);

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
}