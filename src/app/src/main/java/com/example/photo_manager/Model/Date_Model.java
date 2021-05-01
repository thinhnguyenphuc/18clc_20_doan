package com.example.photo_manager.Model;

public class Date_Model {

    private String time;
    private int type;

    public Date_Model(String time, int type){
        this.time = time;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(int type) {
        this.type = type;
    }
}
