package com.example.photo_manager.ui.Album.AlbumDatabase.AlbumPhotoUri;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.photo_manager.ui.PhotoManagerDatabase;

import java.util.List;

public class AlbumPhotoUriReposistory {
    private AlbumPhotoUriDao albumPhotoUriDao;
    private LiveData<List<AlbumPhotoUri>> allAlbumPhotoUris;

    public AlbumPhotoUriReposistory(Application application) {
        PhotoManagerDatabase database = PhotoManagerDatabase.getInstance(application);
        albumPhotoUriDao = database.albumPhotoUriDao();
        allAlbumPhotoUris = albumPhotoUriDao.loadAllAlbumPhotoUris();
    }

    public void insert(AlbumPhotoUri... albumPhotoUris) {
        new InsertAlbumPhotoUriAsyncTask(albumPhotoUriDao).execute(albumPhotoUris);
    }

    public void update(AlbumPhotoUri... albumPhotoUris) {
        new UpdateAlbumPhotoUriAsyncTask(albumPhotoUriDao).execute(albumPhotoUris);
    }

    public void delete(AlbumPhotoUri... albumPhotoUris) {
        new DeleteAlbumPhotoUriAsyncTask(albumPhotoUriDao).execute(albumPhotoUris);
    }

    public void deleteAll() {
        new DeleteAllAlbumPhotoUriAsyncTask(albumPhotoUriDao).execute();
    }

    public LiveData<List<AlbumPhotoUri>> getAllAlbumPhotoUris() {
        return allAlbumPhotoUris;
    }

    private static class InsertAlbumPhotoUriAsyncTask extends AsyncTask<AlbumPhotoUri, Void, Void> {
        private AlbumPhotoUriDao albumPhotoUriDao;

        public InsertAlbumPhotoUriAsyncTask(AlbumPhotoUriDao albumPhotoUriDao) {
            this.albumPhotoUriDao = albumPhotoUriDao;
        }

        @Override
        protected Void doInBackground(AlbumPhotoUri... albumPhotoUris) {
            albumPhotoUriDao.insert(albumPhotoUris);
            return null;
        }
    }

    private static class UpdateAlbumPhotoUriAsyncTask extends AsyncTask<AlbumPhotoUri, Void, Void> {
        private AlbumPhotoUriDao albumPhotoUriDao;

        public UpdateAlbumPhotoUriAsyncTask(AlbumPhotoUriDao albumPhotoUriDao) {
            this.albumPhotoUriDao = albumPhotoUriDao;
        }

        @Override
        protected Void doInBackground(AlbumPhotoUri... albumPhotoUris) {
            albumPhotoUriDao.update(albumPhotoUris);
            return null;
        }
    }

    private static class DeleteAlbumPhotoUriAsyncTask extends AsyncTask<AlbumPhotoUri, Void, Void> {
        private AlbumPhotoUriDao albumPhotoUriDao;

        public DeleteAlbumPhotoUriAsyncTask(AlbumPhotoUriDao albumPhotoUriDao) {
            this.albumPhotoUriDao = albumPhotoUriDao;
        }

        @Override
        protected Void doInBackground(AlbumPhotoUri... albumPhotoUris) {
            albumPhotoUriDao.delete(albumPhotoUris);
            return null;
        }
    }

    private static class DeleteAllAlbumPhotoUriAsyncTask extends AsyncTask<Void, Void, Void> {
        private AlbumPhotoUriDao albumPhotoUriDao;

        public DeleteAllAlbumPhotoUriAsyncTask(AlbumPhotoUriDao albumPhotoUriDao) {
            this.albumPhotoUriDao = albumPhotoUriDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            albumPhotoUriDao.deleteAll();
            return null;
        }
    }
}
