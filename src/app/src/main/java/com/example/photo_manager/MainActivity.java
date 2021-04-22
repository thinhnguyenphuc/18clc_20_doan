package com.example.photo_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class MainActivity extends AppCompatActivity {

    ArrayList<Picture_Model> picture_models = new ArrayList<Picture_Model>();
    ArrayList<Date_Model> date_models = new ArrayList<Date_Model>();
    ArrayList data = new ArrayList<>();


    Button activity1,activity2,activity3;
    Button album_detail;
    Button view_all;

    private static final String permission_read = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String permission_write = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String permission_camera = Manifest.permission.CAMERA;


    private static final SimpleDateFormat fullFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
    private static final SimpleDateFormat onlyDayFormat = new SimpleDateFormat("dd-MM-yyyy");
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
                try {
                    intent.putExtra("images",ImageListToObject().toString());
                    intent.putExtra("time",DateListToObject().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtra("sizeOfPicture",picture_models.size());
                intent.putExtra("sizeOfDate",date_models.size());
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
        view_all =(Button) findViewById(R.id.view_all);
        view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,View_All.class);
                intent.putExtra("sizeOfPicture",picture_models.size());
                try {
                    intent.putExtra("images",ImageListToObject().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermission();


    }
    @Override
    protected void onResume() {
        super.onResume();
        loadImage();
        getTimeFromListPicture();
    }



    private void requestPermission() {
        String[] perms = {permission_read,permission_write, permission_camera};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this,"Must allow to use this app",RequestCode.REQUEST_PERMISSION_CODE,perms);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void loadImage(){
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        picture_models.clear();
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);


        if (cursor != null) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
            while (cursor.moveToNext()) {
                Long idTmp = cursor.getLong(idColumn);
                String nameTmp = cursor.getString(nameColumn);
                int sizeTmp = cursor.getInt(sizeColumn);
                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, idTmp);
                File file = new File(getPath(contentUri));
                String tmpTime =  fullFormat.format(file.lastModified());




                this.picture_models.add(new Picture_Model(contentUri,nameTmp,tmpTime,sizeTmp));
            }
            cursor.close();
        }
    }

    private void getTimeFromListPicture(){
        for(int i=0;i<picture_models.size();i++){
            Picture_Model tmp = picture_models.get(i);
            File file = new File(getPath(tmp.getUri()));
            String tmpTime =  onlyDayFormat.format(file.lastModified());

            ArrayList<String> tmpDate = new ArrayList<String>();
            for(int j =0 ;j<date_models.size();j++){
                tmpDate.add(date_models.get(j).getTime());
            }
            if(!tmpDate.contains(tmpTime)){
                date_models.add(new Date_Model(tmpTime,Type.DATE));
            }
        }
    }

    private JSONObject ImageListToObject() throws JSONException {

        JSONObject objectList = new JSONObject();
        int size = this.picture_models.size();
        for (int i = 0;i<size;i++ ){
            JSONObject pic = new JSONObject();
            pic.put("uri",this.picture_models.get(i).getUri());
            pic.put("name",this.picture_models.get(i).getName());
            pic.put("time",this.picture_models.get(i).getTime());
            pic.put("size",this.picture_models.get(i).getSize());
            objectList.put(String.valueOf(i),pic);
        }
        return objectList;
    }

    private JSONObject DateListToObject() throws JSONException {
        JSONObject objectList = new JSONObject();
        int size = this.date_models.size();
        for (int i = 0;i<size;i++ ){
            JSONObject time = new JSONObject();
            time.put("time",this.date_models.get(i).getTime());
            objectList.put(String.valueOf(i),time);
        }
        return objectList;
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