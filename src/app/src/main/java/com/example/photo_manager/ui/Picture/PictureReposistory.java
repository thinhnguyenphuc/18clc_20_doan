package com.example.photo_manager.ui.Picture;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.photo_manager.Model.Date_Model;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.Model.Video_Model;
import com.example.photo_manager.ProcessData.AsyncResponse;
import com.example.photo_manager.ProcessData.LoadImagesFromStorage;
import com.example.photo_manager.ProcessData.LoadVideoFromStorage;


import java.util.ArrayList;

public class PictureReposistory {
    ArrayList<Picture_Model> picture_models = new ArrayList<Picture_Model>();
    ArrayList<Video_Model> video_models = new ArrayList<Video_Model>();
    ArrayList<Date_Model> date_models = new ArrayList<Date_Model>();

    MutableLiveData<ArrayList<Picture_Model>> pictures = new MutableLiveData<>();
    MutableLiveData<ArrayList<Video_Model>> videos = new MutableLiveData<>();
    MutableLiveData<ArrayList<Date_Model>> dates  = new MutableLiveData<>();

    public PictureReposistory(Context context) {

        LoadImagesFromStorage loadImagesFromStorage = new LoadImagesFromStorage(new AsyncResponse() {
            @Override
            public void processPictureFinish(ArrayList<Picture_Model> pictureModels) {
                picture_models = pictureModels;
                notifyDataChanged();
            }

            @Override
            public void processVideoFinish(ArrayList<Video_Model> video_models) {
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

    public void notifyDataChanged() {
        this.pictures.setValue(picture_models);
    }

    public void update() {
        this.pictures.setValue(picture_models);
    }
    public void updateTakePhoto(Picture_Model picture_model){
        picture_models.add(picture_model);
        this.pictures.setValue(picture_models);
    }
}
