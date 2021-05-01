package com.example.photo_manager.Model;

import android.net.Uri;

import com.example.photo_manager.Type;

import java.util.Date;

public class Picture_Model extends Super_Model{
    public Picture_Model(Uri uri,String name,String time, int size){
        this.setUri(uri);
        this.setName(name);
        this.setTime(time);
        this.setSize(size);
        this.setType(Type.IMAGE);
    }
}
