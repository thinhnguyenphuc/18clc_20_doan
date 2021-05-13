package com.example.photo_manager.ui.Album.AlbumDatabase.Album;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumWithUris;

import java.util.List;

@Dao
public interface AlbumDao {

    @Insert
    void insert(Album... albums);

    @Update
    void update(Album... albums);

    @Delete
    void delete(Album... albums);

    @Query("DELETE FROM Albums")
    void deleteAll();

    @Query("SELECT * FROM Albums")
    LiveData<List<Album>> loadAllAlbums();

    @Transaction
    @Query("SELECT * FROM Albums")
    public LiveData<List<AlbumWithUris>> getAlbumsWithUris();

    @Transaction
    @Query("SELECT * FROM Albums WHERE id = :albumId")
    public LiveData<AlbumWithUris> getAlbumWithUris(int albumId);

    @Transaction
    @Query("DELETE FROM Albums WHERE id=:albumId")
    public void deleteAlbumWhereIdEqual(int albumId);
}
