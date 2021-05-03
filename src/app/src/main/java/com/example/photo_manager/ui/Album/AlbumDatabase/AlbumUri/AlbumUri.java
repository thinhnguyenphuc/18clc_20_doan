package com.example.photo_manager.ui.Album.AlbumDatabase.AlbumUri;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.example.photo_manager.ui.Album.AlbumDatabase.Album.Album;

import static androidx.room.ForeignKey.CASCADE;

@Entity (tableName = "AlbumUris",
        primaryKeys = {"uri", "albumId"},
        foreignKeys= {
        @ForeignKey(entity = Album.class, parentColumns = "id", childColumns = "albumId", onDelete = CASCADE)}
        )
public class AlbumUri {

    @NonNull
    @ColumnInfo(name="uri")
    private String uri;

    public String getUri() {
        return uri;
    }

    public AlbumUri(@NonNull String uri, int albumId, int type) {
        this.uri = uri;
        this.albumId = albumId;
        this.type = type;
    }

    public int getAlbumId() {
        return albumId;
    }

    public int getType() {
        return type;
    }

    @ColumnInfo(name="albumId")
    private int albumId;

    @NonNull
    private int type;
}
