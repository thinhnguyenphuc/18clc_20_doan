package com.example.photo_manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.photo_manager.PEAdapters.Utility;

import java.io.File;
import java.io.IOException;

public class ViewPhoto extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private Picture_Model picture_model = new Picture_Model(null,null,null,0);
    SubsamplingScaleImageView imageView;
    ImageButton back_button, menu_button;
    ImageButton favourite_button, edit_button, share_button, delete_button;
    Toolbar top_toolbar, bottom_toolbar;
    boolean favourite_flag;

    static final int EDIT_PHOTO_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);


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
        menu_button = findViewById(R.id.menu_button);

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
                Toast.makeText(this, "IMAGE IS DELETED", Toast.LENGTH_LONG).show();
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
            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    private boolean deleteImage(Uri uri) {
        String file_dj_path = Utility.getRealPathFromUri(this, uri);
        File fdelete = new File(file_dj_path);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Log.e("-->", "file Deleted :" + file_dj_path);
                this.getContentResolver().delete(picture_model.getUri(), null, null);
                return true;
            } else {
                Log.e("-->", "file not Deleted :" + file_dj_path);
                return false;
            }
        }
        return false;
    }




//    public void callBroadCast() {
//        Log.e("-->", " >= 14");
//        MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
//            /*
//             *   (non-Javadoc)
//             * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
//             */
//            public void onScanCompleted(String path, Uri uri) {
//                Log.e("ExternalStorage", "Scanned " + path + ":");
//                Log.e("ExternalStorage", "-> uri=" + uri);
//            }
//        });
//    }
}