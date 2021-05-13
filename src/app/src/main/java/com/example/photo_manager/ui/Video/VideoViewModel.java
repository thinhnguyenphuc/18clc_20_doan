package com.example.photo_manager.ui.Video;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.photo_manager.Model.Date_Model;
import com.example.photo_manager.Model.Video_Model;

import java.util.ArrayList;

public class VideoViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Video_Model>> videoModels;
    private MutableLiveData<ArrayList<Date_Model>> dateModels;

    VideoReposistory vr;

    public VideoViewModel(@NonNull Application application) {
        super(application);
        this.vr = new VideoReposistory(application);
        videoModels = vr.getAllVideos();
        dateModels = vr.getAllDates();
        Log.d("MY DEBUGGER", "VideoViewModel: ");
    }

    public LiveData<ArrayList<Video_Model>> getAllVideos() {
        return videoModels;
    }
    public LiveData<ArrayList<Date_Model>> getAllDates() {
        return dateModels;
    }

    public void delete(Uri uri) {
        vr.delete(uri);
    }

    public void notifyDataChanged() {
        vr.notifyDataChanged();
    }

    public void update(Context context) {
        vr.update();
    }

    public boolean deleteFromDevice(Context context, Uri uri) {
        return vr.deleteFromStorage(context, uri);
    }

}