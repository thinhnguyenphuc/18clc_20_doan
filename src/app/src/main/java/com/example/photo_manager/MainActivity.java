package com.example.photo_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class MainActivity extends AppCompatActivity {

    Button activity1,activity2,activity3;
    Button album_detail;

    private static final int REQUEST_PERMISSION_CODE = 100;
    private static final String permission_read = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String permission_write = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String permission_camera = Manifest.permission.CAMERA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity1 = (Button) findViewById(R.id.button_activity);

        activity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, album_general.class);
                startActivity(intent);
            }
        });

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
        album_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, album_detail.class);
                startActivity(intent);
            }
        });
        Button take_new_photo = (Button)findViewById(R.id.new_photo);
        take_new_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Take_New_Photo.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermission();
    }


    private void requestPermission() {
        String[] perms = {permission_read,permission_write, permission_camera};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this,"Must allow to use this app",REQUEST_PERMISSION_CODE,perms);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}