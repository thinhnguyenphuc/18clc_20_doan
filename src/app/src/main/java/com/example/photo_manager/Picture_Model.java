package com.example.photo_manager;

import android.net.Uri;

import java.util.Date;

public class Picture_Model {

    private Uri uri;
    private String name;
    private int size;
    private String time;


    public Picture_Model(Uri uri,String name,String time, int size){
        this.uri = uri;
        this.name = name;
        this.time = time;
        this.size = size;
    }

    public Uri getUri(){
        return uri;
    }
    public String getName(){
        return name;
    }
    public int getSize(){
        return size;
    }
    public String getTime(){return time;}
    public void setUri(Uri uri){
        this.uri = uri;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setTime(String time){this.time = time;}
    public void setSize(int size){
        this.size = size;
    }
}
