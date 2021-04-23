package com.example.photo_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity_temp extends AppCompatActivity {
    private static final SimpleDateFormat fullFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
    private static final SimpleDateFormat onlyDayFormat = new SimpleDateFormat("dd-MM-yyyy");
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
        loadImage();
        getTimeFromListPicture();
        Intent intent = new Intent(MainActivity_temp.this,Menu.class);
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

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private JSONObject ImageListToObject() throws JSONException {

        JSONObject objectList = new JSONObject();
        int size = this.picture_models.size();
        for (int i = 0;i<size;i++ ){
            JSONObject pic = new JSONObject();
            pic.put("uri",this.picture_models.get(i).getUri());
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


}