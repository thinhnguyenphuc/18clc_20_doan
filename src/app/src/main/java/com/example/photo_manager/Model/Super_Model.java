package com.example.photo_manager.Model;

import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import com.example.photo_manager.Type;

public class Super_Model{
    private int type;
    private Uri uri;
    private String name;
    private int size;
    private String time;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getTime() {
        return time;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public Uri getUri() {
        return uri;
    }
}
