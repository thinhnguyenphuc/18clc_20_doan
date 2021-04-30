package com.example.photo_manager.ui.Favourite.FavouriteDababase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.photo_manager.ui.Album.AlbumDatabase.Album.Album;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumWithPhotoUris;

import java.util.List;

@Dao
public interface FavouriteItemDao {

    @Insert
    void insert(FavouriteItem... favouriteItems);

    @Update
    void update(FavouriteItem... favouriteItems);

    @Delete
    void delete(FavouriteItem... favouriteItems);

    @Transaction
    @Query("DELETE FROM FavouriteItems")
    void deleteAll();

    @Transaction
    @Query("SELECT * FROM FavouriteItems")
    LiveData<List<FavouriteItem>> loadAllFavouriteItems();

}
