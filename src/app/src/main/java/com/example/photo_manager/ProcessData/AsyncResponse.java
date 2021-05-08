package com.example.photo_manager.ProcessData;

import com.example.photo_manager.Model.Date_Model;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.Model.Video_Model;

import java.util.ArrayList;

public interface AsyncResponse {
    void processPictureFinish(ArrayList<Picture_Model> picture_models);
    void processVideoFinish(ArrayList<Video_Model> video_models);
    void processDateFinish(ArrayList<Date_Model> date_models);
}
