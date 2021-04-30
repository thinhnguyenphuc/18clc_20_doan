package com.example.photo_manager.ui.Album.AlbumDatabase.AlbumPhotoUri;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.example.photo_manager.ui.Album.AlbumDatabase.Album.Album;

import static androidx.room.ForeignKey.CASCADE;

@Entity (tableName = "AlbumPhotoUris",
        primaryKeys = {"uri", "albumId"},
        foreignKeys= {
        @ForeignKey(entity = Album.class, parentColumns = "id", childColumns = "albumId", onDelete = CASCADE)}
        )
public class AlbumPhotoUri {

    @NonNull
    @ColumnInfo(name="uri")
    private String uri;

    public String getUri() {
        return uri;
    }

    public AlbumPhotoUri(@NonNull String uri, int albumId) {
        this.uri = uri;
        this.albumId = albumId;
    }

    public int getAlbumId() {
        return albumId;
    }

    @ColumnInfo(name="albumId")
    private int albumId;
}
