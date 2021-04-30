package com.example.photo_manager.ui;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.photo_manager.ui.Album.AlbumDatabase.Album.Album;
import com.example.photo_manager.ui.Album.AlbumDatabase.Album.AlbumDao;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumPhotoUri.AlbumPhotoUri;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumPhotoUri.AlbumPhotoUriDao;
import com.example.photo_manager.ui.Favourite.FavouriteDababase.FavouriteItem;
import com.example.photo_manager.ui.Favourite.FavouriteDababase.FavouriteItemDao;


@Database(entities = {FavouriteItem.class, Album.class, AlbumPhotoUri.class}, version = 1)
public abstract class PhotoManagerDatabase extends RoomDatabase {
    private static PhotoManagerDatabase instance;

    public abstract AlbumDao albumDao();

    public abstract AlbumPhotoUriDao albumPhotoUriDao();

    public abstract FavouriteItemDao favouriteItemDao();

    public static synchronized PhotoManagerDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), PhotoManagerDatabase.class, "photo_manager_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
