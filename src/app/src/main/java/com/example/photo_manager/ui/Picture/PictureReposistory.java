package com.example.photo_manager.ui.Picture;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.anggrayudi.storage.media.MediaFile;
import com.example.photo_manager.Code.RequestCode;
import com.example.photo_manager.Model.Date_Model;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.Model.Video_Model;
import com.example.photo_manager.ProcessData.AsyncResponse;
import com.example.photo_manager.ProcessData.LoadImagesFromStorage;
import com.example.photo_manager.ProcessData.LoadVideoFromStorage;


import java.util.ArrayList;

import pub.devrel.easypermissions.EasyPermissions;

public class PictureReposistory {
    ArrayList<Picture_Model> picture_models = new ArrayList<Picture_Model>();
    ArrayList<Date_Model> date_models = new ArrayList<Date_Model>();

    MutableLiveData<ArrayList<Picture_Model>> pictures = new MutableLiveData<>();
    MutableLiveData<ArrayList<Date_Model>> dates  = new MutableLiveData<>();

    private static final String permission_read = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String permission_write = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String permission_camera = Manifest.permission.CAMERA;

    public PictureReposistory(Context context) {

        if (!EasyPermissions.hasPermissions(context, permission_read)) {
            return;
        }
        LoadImagesFromStorage loadImagesFromStorage = new LoadImagesFromStorage(new AsyncResponse() {
            @Override
            public void processPictureFinish(ArrayList<Picture_Model> pictureModels) {
                picture_models = pictureModels;
                notifyDataChanged();
            }

            @Override
            public void processVideoFinish(ArrayList<Video_Model> video_models) {
            }

            @Override
            public void processDateFinish(ArrayList<Date_Model> dateModels) {
                date_models= dateModels;
                notifyDataChanged();
            }
        },context);
        loadImagesFromStorage.execute();
        pictures.setValue(picture_models);
        dates.setValue(date_models);

    }

    public MutableLiveData<ArrayList<Picture_Model>> getAllPictures() {
        return pictures;
    }

    public  MutableLiveData<ArrayList<Date_Model>> getAllDates() {
        return dates;
    }

    public void delete(Uri uri) {
        Log.d("my debugger", "before delete: ");
        for (Picture_Model picture_model: picture_models) {
            if (picture_model.getUri().equals(uri)) {
                picture_models.remove(picture_model);
                pictures.setValue(picture_models);
                Log.d("my debugger", "delete: ");
                return;
            }
        }
    }

    public boolean deleteFromStorage(Context context, Uri uri) {
        return new MediaFile(context, uri).delete();
    }

    public void notifyDataChanged() {
        this.pictures.setValue(picture_models);
        this.dates.setValue(date_models);
    }

    public void update(Context context) {

        LoadImagesFromStorage loadImagesFromStorage = new LoadImagesFromStorage(new AsyncResponse() {
            @Override
            public void processPictureFinish(ArrayList<Picture_Model> pictureModels) {
                picture_models = pictureModels;
                notifyDataChanged();
            }

            @Override
            public void processVideoFinish(ArrayList<Video_Model> video_models) {
            }

            @Override
            public void processDateFinish(ArrayList<Date_Model> dateModels) {
                date_models= dateModels;
                notifyDataChanged();
            }
        },context);
        loadImagesFromStorage.execute();
        this.pictures.setValue(picture_models);
    }
    public void updateTakePhoto(Context context, Picture_Model picture_model){
        if (!EasyPermissions.hasPermissions(context, permission_read)) {
            return;
        }
        picture_models.add(picture_model);
        this.pictures.setValue(picture_models);
    }
}
