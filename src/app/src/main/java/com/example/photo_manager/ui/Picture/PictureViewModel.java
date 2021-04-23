package com.example.photo_manager.ui.Picture;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.photo_manager.Date_Model;
import com.example.photo_manager.Picture_Model;

import java.util.ArrayList;

public class PictureViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Picture_Model>> pictureModels;
    private MutableLiveData<ArrayList<Date_Model>> dateModels;

    public PictureViewModel(@NonNull Context context) {
        PictureReposistory pr = new PictureReposistory(context);
        pictureModels = pr.getAllPictures();
        dateModels = pr.getAllDates();
    }

    public LiveData<ArrayList<Picture_Model>> getAllPictures() {
        return pictureModels;
    }
    public LiveData<ArrayList<Date_Model>> getAllDates() {
        return dateModels;
    }


}