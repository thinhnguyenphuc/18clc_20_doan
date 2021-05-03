package com.example.photo_manager;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photo_manager.Adapter.Picture_Adapter;
import com.example.photo_manager.Adapter.View_Adapter;
import com.example.photo_manager.Code.RequestCode;
import com.example.photo_manager.Format.FormatDate;
import com.example.photo_manager.Model.Date_Model;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.Model.Super_Model;
import com.example.photo_manager.Model.Video_Model;
import com.example.photo_manager.ProcessData.AsyncResponse;
import com.example.photo_manager.ProcessData.LoadImagesFromStorage;
import com.example.photo_manager.ProcessData.LoadVideoFromStorage;
import com.example.photo_manager.ProcessData.ProcessData;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity_temp extends ProcessData implements RecyclerViewClickInterface {
    private static final String permission_read = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String permission_write = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String permission_camera = Manifest.permission.CAMERA;
    private RecyclerView recyclerView;
    private ArrayList arrayList  = new ArrayList();
    private View_Adapter view_adapter;
    private ArrayList<Video_Model> video_models;
    private ArrayList<Picture_Model> picture_models;

    MutableLiveData<ArrayList<Picture_Model>> pictures = new MutableLiveData<>();
    MutableLiveData<ArrayList<Video_Model>> videos = new MutableLiveData<>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_temp);
        requestPermission();


        LoadVideoFromStorage loadVideoFromStorage = new LoadVideoFromStorage(new AsyncResponse() {
            @Override
            public void processPictureFinish(ArrayList<Picture_Model> picture_models) {

            }

            @Override
            public void processVideoFinish(ArrayList<Video_Model> videoModels) {
                video_models = videoModels;
            }
        },getApplicationContext());
        LoadImagesFromStorage loadImagesFromStorage = new LoadImagesFromStorage(new AsyncResponse() {
            @Override
            public void processPictureFinish(ArrayList<Picture_Model> pictureModels) {
                picture_models = pictureModels;
            }

            @Override
            public void processVideoFinish(ArrayList<Video_Model> videoModels) {
            }
        },getApplicationContext());
        loadVideoFromStorage.execute();
        loadImagesFromStorage.execute();


        pictures.setValue(picture_models);
        videos.setValue(video_models);

        

        recyclerView = findViewById(R.id.recyclerView);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        toArrayList(video_models, picture_models, arrayList);

        view_adapter = new View_Adapter(this,arrayList,this );

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getApplicationContext());
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        recyclerView.setLayoutManager(flexboxLayoutManager);

        recyclerView.setAdapter(view_adapter);
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

    private void toArrayList(ArrayList<Video_Model> video_models
            ,ArrayList<Picture_Model> picture_models,ArrayList data) {
        for (int i =0 ;i<picture_models.size();i++){
            data.add(picture_models);
        }
        for (int i=0;i<video_models.size();i++){
            data.add(video_models);
        }
    }


    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onLongItemClick(int position) {

    }
}