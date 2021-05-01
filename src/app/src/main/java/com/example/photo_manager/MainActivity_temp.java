package com.example.photo_manager;

import android.Manifest;
import android.os.Bundle;

import com.example.photo_manager.Code.RequestCode;
import com.example.photo_manager.Model.Date_Model;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.ProcessData.LoadFromStorage;
import com.example.photo_manager.ProcessData.ProcessData;

import java.util.ArrayList;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity_temp extends ProcessData {
    ArrayList<Picture_Model> picture_models = new ArrayList<Picture_Model>();
    ArrayList<Date_Model> date_models = new ArrayList<Date_Model>();
    private static final String permission_read = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String permission_write = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String permission_camera = Manifest.permission.CAMERA;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_temp);
        requestPermission();
//        Intent intent = new Intent(MainActivity_temp.this,Menu.class);
//        try {
//            intent.putExtra("images",ImageListToObject(picture_models).toString());
//            intent.putExtra("time",DateListToObject(date_models).toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        intent.putExtra("sizeOfPicture",picture_models.size());
//        intent.putExtra("sizeOfDate",date_models.size());
//        startActivity(intent);
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


}