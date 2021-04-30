package com.example.photo_manager.ui.Album.AlbumDatabase.AlbumPhotoUri;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.photo_manager.ui.Album.AlbumDatabase.Album.Album;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface AlbumPhotoUriDao {

    @Insert
    void insert(AlbumPhotoUri... albumPhotoUris);

    @Update
    void update(AlbumPhotoUri... albumPhotoUris);

    @Delete
    void delete(AlbumPhotoUri... albumPhotoUris);

    @Query("DELETE FROM AlbumPhotoUris")
    void deleteAll();

    @Query("SELECT * FROM AlbumPhotoUris")
    LiveData<List<AlbumPhotoUri>> loadAllAlbumPhotoUris();

    @Query("SELECT * FROM AlbumPhotoUris WHERE albumId= :id")
    LiveData<List<AlbumPhotoUri>> loadUriOfAlbumWithId(int id) ;
}
