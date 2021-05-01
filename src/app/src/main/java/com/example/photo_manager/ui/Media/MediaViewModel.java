package com.example.photo_manager.ui.Media;

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

public class MediaViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Picture_Model>> pictureModels;
    private MutableLiveData<ArrayList<Date_Model>> dateModels;

    MediaReposistory pr;

    public MediaViewModel(@NonNull Application application) {
        super(application);
        this.pr = new MediaReposistory(application);
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

    public void notifyDataChanged() {
        pr.notifyDataChanged();
    }

    public void update(Context context) {
        pr.update(context);
    }

}