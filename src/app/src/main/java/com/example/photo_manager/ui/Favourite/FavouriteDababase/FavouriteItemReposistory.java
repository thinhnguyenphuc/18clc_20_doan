package com.example.photo_manager.ui.Favourite.FavouriteDababase;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.photo_manager.ui.PhotoManagerDatabase;

import java.util.List;

public class FavouriteItemReposistory {
    private FavouriteItemDao favouriteItemDao;
    private LiveData<List<FavouriteItem>> allFavouriteItems;

    public FavouriteItemReposistory(Application application) {
        PhotoManagerDatabase database = PhotoManagerDatabase.getInstance(application);
        favouriteItemDao = database.favouriteItemDao();
        allFavouriteItems = favouriteItemDao.loadAllFavouriteItems();
    }

    public void insert(FavouriteItem... FavouriteItems) {
        new InsertFavouriteItemAsyncTask(favouriteItemDao).execute(FavouriteItems);
    }

    public void update(FavouriteItem... FavouriteItems) {
        new UpdateFavouriteItemAsyncTask(favouriteItemDao).execute(FavouriteItems);
    }

    public void delete(FavouriteItem... FavouriteItems) {
        new DeleteFavouriteItemAsyncTask(favouriteItemDao).execute(FavouriteItems);
    }

    public void deleteAll() {
        new DeleteAllFavouriteItemAsyncTask(favouriteItemDao).execute();
    }

    public LiveData<List<FavouriteItem>> getAllFavouriteItems() {
        return allFavouriteItems;
    }

    public boolean checkUriExistence(String uri) {
        return favouriteItemDao.checkUriExistence(uri);
    }

    private static class InsertFavouriteItemAsyncTask extends AsyncTask<FavouriteItem, Void, Void> {
        private FavouriteItemDao favouriteItemDao;

        public InsertFavouriteItemAsyncTask(FavouriteItemDao favouriteItemDao) {
            try {
                this.favouriteItemDao = favouriteItemDao;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(FavouriteItem... FavouriteItems) {
            favouriteItemDao.insert(FavouriteItems);
            return null;
        }
    }

    private static class UpdateFavouriteItemAsyncTask extends AsyncTask<FavouriteItem, Void, Void> {
        private FavouriteItemDao favouriteItemDao;

        public UpdateFavouriteItemAsyncTask(FavouriteItemDao favouriteItemDao) {
            this.favouriteItemDao = favouriteItemDao;
        }

        @Override
        protected Void doInBackground(FavouriteItem... FavouriteItems) {
            favouriteItemDao.update(FavouriteItems);
            return null;
        }
    }

    private static class DeleteFavouriteItemAsyncTask extends AsyncTask<FavouriteItem, Void, Void> {
        private FavouriteItemDao favouriteItemDao;

        public DeleteFavouriteItemAsyncTask(FavouriteItemDao favouriteItemDao) {
            this.favouriteItemDao = favouriteItemDao;
        }

        @Override
        protected Void doInBackground(FavouriteItem... FavouriteItems) {
            favouriteItemDao.delete(FavouriteItems);
            return null;
        }
    }

    private static class DeleteAllFavouriteItemAsyncTask extends AsyncTask<Void, Void, Void> {
        private FavouriteItemDao favouriteItemDao;

        public DeleteAllFavouriteItemAsyncTask(FavouriteItemDao favouriteItemDao) {
            this.favouriteItemDao = favouriteItemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            favouriteItemDao.deleteAll();
            return null;
        }
    }
}