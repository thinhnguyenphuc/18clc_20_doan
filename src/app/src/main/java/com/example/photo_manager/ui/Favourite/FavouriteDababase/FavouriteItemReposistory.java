package com.example.photo_manager.ui.Favourite.FavouriteDababase;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.photo_manager.ui.PhotoManagerDatabase;

import java.util.List;

public class FavouriteItemReposistory {
    private FavouriteItemDao FavouriteItemDao;
    private LiveData<List<FavouriteItem>> allFavouriteItems;

    public FavouriteItemReposistory(Application application) {
        PhotoManagerDatabase database = PhotoManagerDatabase.getInstance(application);
        FavouriteItemDao = database.favouriteItemDao();
        allFavouriteItems = FavouriteItemDao.loadAllFavouriteItems();
    }

    public void insert(FavouriteItem... FavouriteItems) {
        new InsertFavouriteItemAsyncTask(FavouriteItemDao).execute(FavouriteItems);
    }

    public void update(FavouriteItem... FavouriteItems) {
        new UpdateFavouriteItemAsyncTask(FavouriteItemDao).execute(FavouriteItems);
    }

    public void delete(FavouriteItem... FavouriteItems) {
        new DeleteFavouriteItemAsyncTask(FavouriteItemDao).execute(FavouriteItems);
    }

    public void deleteAll() {
        new DeleteAllFavouriteItemAsyncTask(FavouriteItemDao).execute();
    }

    public LiveData<List<FavouriteItem>> getAllFavouriteItems() {
        return allFavouriteItems;
    }

    private static class InsertFavouriteItemAsyncTask extends AsyncTask<FavouriteItem, Void, Void> {
        private FavouriteItemDao FavouriteItemDao;

        public InsertFavouriteItemAsyncTask(FavouriteItemDao FavouriteItemDao) {
            this.FavouriteItemDao = FavouriteItemDao;
        }

        @Override
        protected Void doInBackground(FavouriteItem... FavouriteItems) {
            FavouriteItemDao.insert(FavouriteItems);
            return null;
        }
    }

    private static class UpdateFavouriteItemAsyncTask extends AsyncTask<FavouriteItem, Void, Void> {
        private FavouriteItemDao FavouriteItemDao;

        public UpdateFavouriteItemAsyncTask(FavouriteItemDao FavouriteItemDao) {
            this.FavouriteItemDao = FavouriteItemDao;
        }

        @Override
        protected Void doInBackground(FavouriteItem... FavouriteItems) {
            FavouriteItemDao.update(FavouriteItems);
            return null;
        }
    }

    private static class DeleteFavouriteItemAsyncTask extends AsyncTask<FavouriteItem, Void, Void> {
        private FavouriteItemDao FavouriteItemDao;

        public DeleteFavouriteItemAsyncTask(FavouriteItemDao FavouriteItemDao) {
            this.FavouriteItemDao = FavouriteItemDao;
        }

        @Override
        protected Void doInBackground(FavouriteItem... FavouriteItems) {
            FavouriteItemDao.delete(FavouriteItems);
            return null;
        }
    }

    private static class DeleteAllFavouriteItemAsyncTask extends AsyncTask<Void, Void, Void> {
        private FavouriteItemDao FavouriteItemDao;

        public DeleteAllFavouriteItemAsyncTask(FavouriteItemDao FavouriteItemDao) {
            this.FavouriteItemDao = FavouriteItemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            FavouriteItemDao.deleteAll();
            return null;
        }
    }
}