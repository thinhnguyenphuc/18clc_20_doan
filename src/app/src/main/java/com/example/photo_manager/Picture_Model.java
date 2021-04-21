package com.example.photo_manager;

import android.net.Uri;

public class Picture_Model {

    private Uri uri;
    private String name;
    private int size;


    public Picture_Model(Uri uri,String name, int size){
        this.uri = uri;
        this.name = name;
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
    public void setUri(Uri uri){
        this.uri = uri;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setSize(int size){
        this.size = size;
    }
}
