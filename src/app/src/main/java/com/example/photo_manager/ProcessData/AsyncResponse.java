package com.example.photo_manager.ProcessData;

import com.example.photo_manager.Picture_Model;

import java.util.ArrayList;

public interface AsyncResponse {
    void processFinish(ArrayList<Picture_Model> picture_models);
}
