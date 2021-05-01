package com.example.photo_manager.ProcessData;

import com.example.photo_manager.Model.Picture_Model;

import java.util.ArrayList;

public interface AsyncResponse {
    void processPictureFinish(ArrayList<Picture_Model> picture_models);
}
