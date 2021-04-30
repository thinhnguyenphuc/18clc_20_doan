package com.example.photo_manager.ui.Album.AlbumDatabase.Album;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.photo_manager.ui.PhotoManagerDatabase;

import java.util.List;

public class AlbumReposistory {
    private AlbumDao albumDao;
    private LiveData<List<Album>> allAlbums;

    public AlbumReposistory(Application application) {
        PhotoManagerDatabase database = PhotoManagerDatabase.getInstance(application);
        albumDao = database.albumDao();
        allAlbums = albumDao.loadAllAlbums();
    }

    public void insert(Album... albums) {
        new InsertAlbumAsyncTask(albumDao).execute(albums);
    }

    public void update(Album... albums) {
        new UpdateAlbumAsyncTask(albumDao).execute(albums);
    }

    public void delete(Album... albums) {
        new DeleteAlbumAsyncTask(albumDao).execute(albums);
    }

    public void deleteAll() {
        new DeleteAllAlbumAsyncTask(albumDao).execute();
    }

    public LiveData<List<Album>> getAllAlbums() {
        return allAlbums;
    }

    private static class InsertAlbumAsyncTask extends AsyncTask<Album, Void, Void> {
        private AlbumDao albumDao;

        public InsertAlbumAsyncTask(AlbumDao albumDao) {
            this.albumDao = albumDao;
        }

        @Override
        protected Void doInBackground(Album... albums) {
            albumDao.insert(albums);
            return null;
        }
    }

    private static class UpdateAlbumAsyncTask extends AsyncTask<Album, Void, Void> {
        private AlbumDao albumDao;

        public UpdateAlbumAsyncTask(AlbumDao albumDao) {
            this.albumDao = albumDao;
        }

        @Override
        protected Void doInBackground(Album... albums) {
            albumDao.update(albums);
            return null;
        }
    }

    private static class DeleteAlbumAsyncTask extends AsyncTask<Album, Void, Void> {
        private AlbumDao albumDao;

        public DeleteAlbumAsyncTask(AlbumDao albumDao) {
            this.albumDao = albumDao;
        }

        @Override
        protected Void doInBackground(Album... albums) {
            albumDao.delete(albums);
            return null;
        }
    }

    private static class DeleteAllAlbumAsyncTask extends AsyncTask<Void, Void, Void> {
        private AlbumDao albumDao;

        public DeleteAllAlbumAsyncTask(AlbumDao albumDao) {
            this.albumDao = albumDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            albumDao.deleteAll();
            return null;
        }
    }
}
