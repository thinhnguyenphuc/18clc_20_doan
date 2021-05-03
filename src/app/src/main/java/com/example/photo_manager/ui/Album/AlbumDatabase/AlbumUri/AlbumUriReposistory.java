package com.example.photo_manager.ui.Album.AlbumDatabase.AlbumUri;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.photo_manager.ui.PhotoManagerDatabase;

import java.util.List;

public class AlbumUriReposistory {
    private AlbumUriDao albumUriDao;
    private LiveData<List<AlbumUri>> allAlbumUris;

    public AlbumUriReposistory(Application application) {
        PhotoManagerDatabase database = PhotoManagerDatabase.getInstance(application);
        albumUriDao = database.albumPhotoUriDao();
        allAlbumUris = albumUriDao.loadAllAlbumPhotoUris();
    }

    public void insert(AlbumUri... albumUris) {
        new InsertAlbumPhotoUriAsyncTask(albumUriDao).execute(albumUris);
    }

    public void update(AlbumUri... albumUris) {
        new UpdateAlbumPhotoUriAsyncTask(albumUriDao).execute(albumUris);
    }

    public void delete(AlbumUri... albumUris) {
        new DeleteAlbumPhotoUriAsyncTask(albumUriDao).execute(albumUris);
    }

    public void deleteAll() {
        new DeleteAllAlbumPhotoUriAsyncTask(albumUriDao).execute();
    }

    public LiveData<List<AlbumUri>> getAllAlbumUris() {
        return allAlbumUris;
    }

    private static class InsertAlbumPhotoUriAsyncTask extends AsyncTask<AlbumUri, Void, Void> {
        private AlbumUriDao albumUriDao;

        public InsertAlbumPhotoUriAsyncTask(AlbumUriDao albumUriDao) {
            this.albumUriDao = albumUriDao;
        }

        @Override
        protected Void doInBackground(AlbumUri... albumUris) {
            albumUriDao.insert(albumUris);
            return null;
        }
    }

    private static class UpdateAlbumPhotoUriAsyncTask extends AsyncTask<AlbumUri, Void, Void> {
        private AlbumUriDao albumUriDao;

        public UpdateAlbumPhotoUriAsyncTask(AlbumUriDao albumUriDao) {
            this.albumUriDao = albumUriDao;
        }

        @Override
        protected Void doInBackground(AlbumUri... albumUris) {
            albumUriDao.update(albumUris);
            return null;
        }
    }

    private static class DeleteAlbumPhotoUriAsyncTask extends AsyncTask<AlbumUri, Void, Void> {
        private AlbumUriDao albumUriDao;

        public DeleteAlbumPhotoUriAsyncTask(AlbumUriDao albumUriDao) {
            this.albumUriDao = albumUriDao;
        }

        @Override
        protected Void doInBackground(AlbumUri... albumUris) {
            albumUriDao.delete(albumUris);
            return null;
        }
    }

    private static class DeleteAllAlbumPhotoUriAsyncTask extends AsyncTask<Void, Void, Void> {
        private AlbumUriDao albumUriDao;

        public DeleteAllAlbumPhotoUriAsyncTask(AlbumUriDao albumUriDao) {
            this.albumUriDao = albumUriDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            albumUriDao.deleteAll();
            return null;
        }
    }
}
