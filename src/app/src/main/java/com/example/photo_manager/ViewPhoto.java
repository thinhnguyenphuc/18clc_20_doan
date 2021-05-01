package com.example.photo_manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.photo_manager.Code.ResultCode;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.PEAdapters.Utility;

public class ViewPhoto extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private Picture_Model picture_model = new Picture_Model(null,null,null,0);
    SubsamplingScaleImageView imageView;
    ImageButton back_button, menu_button;
    ImageButton favourite_button, edit_button, share_button, delete_button;
    Toolbar top_toolbar, bottom_toolbar;
    boolean favourite_flag;

    static final int EDIT_PHOTO_REQUEST = 1;
    boolean EDIT_PHOTO_FLAG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_photo);


        Intent receiver = getIntent();
        String tmp = receiver.getStringExtra("uri");

        picture_model.setUri(Uri.parse(tmp));

        imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);

        try {
            imageView.setImage(ImageSource.uri(picture_model.getUri()));
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }

        //Glide.with(this).load(picture_model.getUri()).into(imageView);


        top_toolbar = findViewById(R.id.toolbar_top);
        bottom_toolbar = findViewById(R.id.toolbar_bottom);

        back_button = findViewById(R.id.back_button);


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


        this.back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EDIT_PHOTO_FLAG) {
                    setResult(ResultCode.RESULT_VIEW_PHOTO_EDITED);
                }
                finish();
            }
        });

        this.menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
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
                intent.putExtra("uri", getIntent().getStringExtra("uri"));

                startActivityForResult(intent, EDIT_PHOTO_REQUEST);
            }
        });

        findViewById(R.id.delete_button).setOnClickListener(v -> {
            if (deleteImage(picture_model.getUri())) {
                Intent returnData = new Intent();
                returnData.putExtra("uri", picture_model.getUri().toString());
                Toast.makeText(this, "IMAGE IS DELETED", Toast.LENGTH_LONG).show();
                setResult(ResultCode.RESULT_VIEW_PHOTO_DELETED, returnData);
                finish();
            } else {
                Toast.makeText(this, "FAILED TO DELETE IMAGE", Toast.LENGTH_LONG).show();
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

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.vp_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.vp_menu_photo_detail:
                openPhotoDetail();
                return true;
            case R.id.vp_menu_wallpaper:
                return true;
            case R.id.vp_menu_security_folder:
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PHOTO_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                imageView.setImage(ImageSource.uri(picture_model.getUri()));
                EDIT_PHOTO_FLAG = true;
            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    private boolean deleteImage(Uri uri) {
        this.getContentResolver().delete(picture_model.getUri(), null, null);
        return true;
    }

    private void openPhotoDetail() {

        Intent view_details = new Intent(ViewPhoto.this, Photo_Details.class);
        view_details.putExtra("uri",picture_model.getUri().toString());
        view_details.putExtra("name",picture_model.getName());
        view_details.putExtra("time",picture_model.getTime());
        view_details.putExtra("size",picture_model.getSize());
        startActivity(view_details);
    }
}