package com.example.photo_manager.Model;

import android.net.Uri;

import com.example.photo_manager.Type;

public class Video_Model extends Super_Model{
    private long duration;
    public Video_Model(Uri uri,String name,String time, int size,long duration){
        this.setUri(uri);
        this.setName(name);
        this.setTime(time);
        this.setSize(size);
        this.setType(Type.VIDEO);
        this.duration = duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }
}

