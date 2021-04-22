package com.example.photo_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import pub.devrel.easypermissions.EasyPermissions;

public class Menu extends AppCompatActivity {
    ArrayList<Picture_Model> picture_models = new ArrayList<Picture_Model>();
    ArrayList<Date_Model> date_models = new ArrayList<Date_Model>();
    private static final SimpleDateFormat fullFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

    Button activity1,activity2,activity3;
    Button album_detail;
    Button view_all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent receiver =getIntent();
        getReceiver(receiver);

        activity1 = (Button) findViewById(R.id.button_activity);

        activity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, album_general.class);
                startActivity(intent);
            }
        });

        activity2 = (Button) findViewById(R.id.button_activity2);

        activity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Photo_Details.class);
                startActivity(intent);
            }
        });

        activity3 = (Button) findViewById(R.id.button_activity3);

        activity3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, View_By_Date.class);
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
                Intent intent = new Intent(Menu.this, album_detail.class);
                startActivity(intent);
            }
        });

        Button take_new_photo = (Button)findViewById(R.id.new_photo);

        take_new_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Take_New_Photo.class);
                startActivityForResult(intent,RequestCode.REQUEST_INTENT_TAKE_NEW_PHOTO);
            }
        });

        view_all =(Button) findViewById(R.id.view_all);

        view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,View_All.class);
                intent.putExtra("sizeOfPicture",picture_models.size());
                try {
                    intent.putExtra("images",ImageListToObject().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivityForResult(intent,RequestCode.REQUEST_INTENT_VIEW_ALL);
            }
        });
    }

    private void getReceiver(Intent receiver){
        int sizeOfPicture = receiver.getIntExtra("sizeOfPicture",0);

        try {
            JSONObject tmpListObject = new JSONObject(receiver.getStringExtra("images"));
            for(int i = 0 ;i<sizeOfPicture;i++){
                JSONObject tmpObject = new JSONObject(tmpListObject.getString(String.valueOf(i)));
                Uri tmpUri = Uri.parse(tmpObject.get("uri").toString());


                picture_models.add(new Picture_Model(tmpUri,null,null,0));
            }
        } catch (JSONException  e) {
            e.printStackTrace();
        }

        int sizeOfDate = receiver.getIntExtra("sizeOfDate",0);
        try {
            JSONObject tmpListObject = new JSONObject(receiver.getStringExtra("time"));
            for(int i = 0 ;i<sizeOfDate;i++){
                JSONObject tmpObject = new JSONObject(tmpListObject.getString(String.valueOf(i)));
                String tmpTime = tmpObject.get("time").toString();

                date_models.add(new Date_Model(tmpTime,Type.DATE));
            }
        } catch (JSONException  e) {
            e.printStackTrace();
        }
    }

    private JSONObject ImageListToObject() throws JSONException {

        JSONObject objectList = new JSONObject();
        int size = this.picture_models.size();
        for (int i = 0;i<size;i++ ){
            JSONObject pic = new JSONObject();
            pic.put("uri",this.picture_models.get(i).getUri());
            pic.put("time",this.picture_models.get(i).getTime());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode.REQUEST_INTENT_TAKE_NEW_PHOTO) {
            if(resultCode==RESULT_OK){
                Uri tmp = Uri.parse(data.getStringExtra("path"));
                File file = new File(getPath(tmp));
                String tmpTime =  fullFormat.format(file.lastModified());
                picture_models.add(new Picture_Model(tmp,null,tmpTime,0));
            }
        }
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