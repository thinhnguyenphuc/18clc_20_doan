package com.example.photo_manager.ui.Album.AlbumDatabase;

import androidx.room.Embedded;
import androidx.room.Query;
import androidx.room.Relation;
import androidx.room.Transaction;

import com.example.photo_manager.ui.Album.AlbumDatabase.Album.Album;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumPhotoUri.AlbumPhotoUri;

import java.util.List;

public class AlbumWithPhotoUris {
    @Embedded public Album album;

    @Relation(
            parentColumn = "id",
            entityColumn = "albumId"
    )
    public List<AlbumPhotoUri> albumPhotoUris;
}
