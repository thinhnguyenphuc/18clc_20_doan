package com.example.photo_manager.ui.Album.AlbumDatabase.AlbumUri;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AlbumUriDao {

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    void insert(AlbumUri... albumUris);

    @Update
    void update(AlbumUri... albumUris);

    @Delete
    void delete(AlbumUri... albumUris);

    @Query("DELETE FROM AlbumUris")
    void deleteAll();

    @Query("SELECT * FROM AlbumUris")
    LiveData<List<AlbumUri>> loadAllAlbumPhotoUris();

    @Query("SELECT * FROM AlbumUris WHERE albumId= :id")
    LiveData<List<AlbumUri>> loadUriOfAlbumWithId(int id) ;
}
