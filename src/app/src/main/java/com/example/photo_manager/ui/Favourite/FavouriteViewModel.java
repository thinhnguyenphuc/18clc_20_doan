package com.example.photo_manager.ui.Favourite;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.photo_manager.ui.Favourite.FavouriteDababase.FavouriteItem;
import com.example.photo_manager.ui.Favourite.FavouriteDababase.FavouriteItemReposistory;

import java.util.List;

public class FavouriteViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    LiveData<List<FavouriteItem>> favouriteItems;
    FavouriteItemReposistory reposistory;

    public FavouriteViewModel(@NonNull Application application) {
        super(application);
        reposistory = new FavouriteItemReposistory(application);
        favouriteItems = reposistory.getAllFavouriteItems();
    }

    public void insert(FavouriteItem... favouriteItems) {
        reposistory.insert(favouriteItems);
    }

    public void update(FavouriteItem... favouriteItems) {
        reposistory.update(favouriteItems);
    }

    public void delete(FavouriteItem... favouriteItems) {
        reposistory.delete(favouriteItems);
    }

    public void deleteAll() {
        reposistory.deleteAll();
    }

   public LiveData<List<FavouriteItem>> getAllFavouriteItems() {
        return favouriteItems;
    }

    public boolean checkUriExistence(String uri) {
        return reposistory.checkUriExistence(uri);
    }

}