package com.example.photo_manager.ui.Album.AlbumDatabase;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.photo_manager.ui.Album.AlbumDatabase.Album.Album;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumUri.AlbumUri;

import java.util.List;

public class AlbumWithUris {
    @Embedded public Album album;

    @Relation(
            parentColumn = "id",
            entityColumn = "albumId"
    )
    public List<AlbumUri> albumUris;
}
