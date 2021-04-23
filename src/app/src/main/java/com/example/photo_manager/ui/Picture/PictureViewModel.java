package com.example.photo_manager.ui.Picture;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.photo_manager.Date_Model;
import com.example.photo_manager.Picture_Model;

import java.util.ArrayList;

public class PictureViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Picture_Model>> pictureModels;
    private MutableLiveData<ArrayList<Date_Model>> dateModels;

    public PictureViewModel(@NonNull Application application) {
        super(application);
        PictureReposistory pr = new PictureReposistory(application);
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