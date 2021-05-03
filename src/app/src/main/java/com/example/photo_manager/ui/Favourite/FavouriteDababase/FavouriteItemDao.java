package com.example.photo_manager.ui.Favourite.FavouriteDababase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavouriteItemDao {


    @Insert (onConflict = OnConflictStrategy.REPLACE)
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

    @Transaction
    @Query("SELECT EXISTS (SELECT 1 FROM FavouriteItems WHERE uri = :uri)")
    boolean checkUriExistence(String uri);

}
