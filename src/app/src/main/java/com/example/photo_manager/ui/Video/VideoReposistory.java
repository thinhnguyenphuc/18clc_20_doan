package com.example.photo_manager.ui.Video;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.anggrayudi.storage.media.MediaFile;
import com.example.photo_manager.Model.Date_Model;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.Model.Video_Model;
import com.example.photo_manager.ProcessData.AsyncResponse;
import com.example.photo_manager.ProcessData.LoadVideoFromStorage;

import java.util.ArrayList;

public class VideoReposistory {
    ArrayList<Video_Model> video_models = new ArrayList<>();

    ArrayList<Date_Model> date_models = new ArrayList<>();


    MutableLiveData<ArrayList<Video_Model>> videos = new MutableLiveData<>();
    MutableLiveData<ArrayList<Date_Model>> dates = new MutableLiveData<>();

    public VideoReposistory(Context context) {
        LoadVideoFromStorage loadVideoFromStorage = new LoadVideoFromStorage(new AsyncResponse() {
            @Override
            public void processPictureFinish(ArrayList<Picture_Model> picture_models) {

            }

            @Override
            public void processVideoFinish(ArrayList<Video_Model> videoModels) {
                video_models = videoModels;
                notifyDataChanged();
            }

            @Override
            public void processDateFinish(ArrayList<Date_Model> dateModels) {
                date_models = dateModels;
                notifyDataChanged();
            }
        }, context);
        loadVideoFromStorage.execute();
        videos.setValue(video_models);
        dates.setValue(date_models);
    }

    public MutableLiveData<ArrayList<Video_Model>> getAllVideos() {
        return videos;
    }

    public MutableLiveData<ArrayList<Date_Model>> getAllDates() {
        return dates;
    }

    public void delete(Uri uri) {
        Log.d("my debugger", "before delete: ");
        for (Video_Model video_model : video_models) {
            if (video_model.getUri().equals(uri)) {
                video_models.remove(video_model);
                videos.setValue(video_models);
                Log.d("my debugger", "delete: ");
                return;
            }
        }
    }

    public void notifyDataChanged() {
        this.videos.setValue(video_models);
        this.dates.setValue(date_models);
    }

    public void update() {
        this.videos.setValue(video_models);
    }

    public boolean deleteFromStorage(Context context, Uri uri) {
        return new MediaFile(context, uri).delete();
    }
}
