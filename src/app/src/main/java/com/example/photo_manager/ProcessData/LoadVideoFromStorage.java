package com.example.photo_manager.ProcessData;

import android.os.AsyncTask;

import com.example.photo_manager.Model.Video_Model;

import java.util.ArrayList;

public class LoadVideoFromStorage extends AsyncTask<Void, Integer, ArrayList<Video_Model>> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Video_Model> doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ArrayList<Video_Model> video_models) {
        super.onPostExecute(video_models);
    }
}
