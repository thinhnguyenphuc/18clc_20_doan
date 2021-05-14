package com.example.photo_manager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.photo_manager.Code.RequestCode;
import com.example.photo_manager.ui.Picture.PictureViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final String permission_read = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String permission_write = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String permission_camera = Manifest.permission.CAMERA;
    private static final String permission_gps = Manifest.permission.ACCESS_FINE_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setAppTheme();

        setContentView(R.layout.activity_main);
        requestPermission();

        Log.d("DEBUG", "onCreate: permission accepted");
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

        String[] perms = {permission_read,permission_write, permission_camera, permission_gps};
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
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Some permissions have been granted
        // ...
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied
        // ...
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    private void setAppTheme() {
        SharedPreferences sharedPreferences = this
                .getSharedPreferences("com.example.photo_manager.settings", Context.MODE_PRIVATE);
        int theme = sharedPreferences.getInt("theme", 0);

        switch (theme) {
            case 1:
            {
                setTheme(R.style.AppTheme1);
                break;
            }
            case 2:
            {
                setTheme(R.style.AppTheme2);
                break;
            }
            default:
            {
                setTheme(R.style.AppTheme);
                break;
            }
        }
    }

}