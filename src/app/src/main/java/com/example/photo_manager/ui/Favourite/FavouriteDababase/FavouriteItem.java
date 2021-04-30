package com.example.photo_manager.ui.Favourite.FavouriteDababase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "FavouriteItems")
public class FavouriteItem {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    String uri;

    int type;

    public FavouriteItem(String uri, int type) {
        this.uri = uri;
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public int getType() {
        return type;
    }

}
