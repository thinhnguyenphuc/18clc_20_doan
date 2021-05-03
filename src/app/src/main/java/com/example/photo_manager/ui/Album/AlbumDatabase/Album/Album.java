package com.example.photo_manager.ui.Album.AlbumDatabase.Album;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;

@Entity(tableName = "Albums",
        indices = {@Index(value = "title", unique = true)}
        )
public class Album {

    @ColumnInfo(name="id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="title")
    private String title;

    public Album(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setId(int id) {
        this.id = id;
    }

}
