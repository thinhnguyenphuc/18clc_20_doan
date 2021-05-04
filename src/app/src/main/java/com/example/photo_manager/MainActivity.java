package com.example.photo_manager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.photo_manager.Code.RequestCode;
import com.example.photo_manager.ui.Picture.PictureViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private static final String permission_read = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String permission_write = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String permission_camera = Manifest.permission.CAMERA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        requestPermission();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupWithNavController(navView, navController);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void requestPermission() {

        String[] perms = {permission_read,permission_write, permission_camera};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this,"Must allow to use this app", RequestCode.REQUEST_PERMISSION_CODE,perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MAIN", "onRestart: ");
        //re-create
        PictureViewModel pictureViewModel =
                new ViewModelProvider(this).get(PictureViewModel.class);

        pictureViewModel.update(this);
    }
}