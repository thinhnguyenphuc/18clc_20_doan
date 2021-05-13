package com.example.photo_manager.ui.Picture;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.photo_manager.Model.Date_Model;
import com.example.photo_manager.Model.Picture_Model;

import java.util.ArrayList;

public class PictureViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Picture_Model>> pictureModels;
    private MutableLiveData<ArrayList<Date_Model>> dateModels;

    PictureReposistory pr;

    public PictureViewModel(@NonNull Application application) {
        super(application);
        this.pr = new PictureReposistory(application);
        pictureModels = pr.getAllPictures();
        dateModels = pr.getAllDates();
        Log.d("MY DEBUGGER", "PictureViewModel: ");
    }

    public LiveData<ArrayList<Picture_Model>> getAllPictures() {
        return pictureModels;
    }
    public LiveData<ArrayList<Date_Model>> getAllDates() {
        return dateModels;
    }

    public void delete(Uri uri) {
        pr.delete(uri);
    }

    public boolean deleteFromDevice(Context context, Uri uri) {
        return pr.deleteFromStorage(context, uri);
    }

    public void notifyDataChanged() {
        pr.notifyDataChanged();
    }

    public void update(Context context) {
        pr.update();
    }
    public void updateTakeNewPhoto(Picture_Model picture_model){
        pr.updateTakePhoto(picture_model);
    }

    public void deleteImages(Context context, ArrayList<Picture_Model> picture_models) {
        for (Picture_Model picture_model: picture_models) {
            if (deleteFromDevice(context, picture_model.getUri())) {
                delete(picture_model.getUri());
            }
        }
    }

}