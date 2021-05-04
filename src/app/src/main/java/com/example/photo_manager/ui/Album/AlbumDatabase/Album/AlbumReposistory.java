package com.example.photo_manager.ui.Album.AlbumDatabase.Album;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumWithUris;
import com.example.photo_manager.ui.PhotoManagerDatabase;

import java.util.List;

public class AlbumReposistory {
    private AlbumDao albumDao;
    private LiveData<List<Album>> allAlbums;
    private LiveData<List<AlbumWithUris>> allAlbumsWithUris;

    public AlbumReposistory(Application application) {
        PhotoManagerDatabase database = PhotoManagerDatabase.getInstance(application);
        albumDao = database.albumDao();
        allAlbums = albumDao.loadAllAlbums();
    }

    public void insert(Album... albums) {
        Log.d("ALBUM REPOSISTORY", "insert: ");
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

    public void deleteAlbumWhereIdEqual(int albumId) {
        new DeleteAlbumWhereIdAsynTask(albumDao).execute(albumId);
    }

    public LiveData<List<Album>> getAllAlbums() {
        return allAlbums;
    }

    public LiveData<List<AlbumWithUris>> getAllAlbumsWithUris() {
        Log.d("ALBUM REPOSISTORY", "getAllAlbumsWithUris: ");
        return albumDao.getAlbumsWithUris();
    }

    public LiveData<AlbumWithUris> getAlbumWithUris(int albumId) {
        return albumDao.getAlbumWithUris(albumId);
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

    private static class DeleteAlbumWhereIdAsynTask extends AsyncTask<Integer, Void, Void> {
        private AlbumDao albumDao;

        public DeleteAlbumWhereIdAsynTask(AlbumDao albumDao) {
            this.albumDao = albumDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            if (integers.length == 1) {
                albumDao.deleteAlbumWhereIdEqual(integers[0]);
            }
            return null;
        }
    }
}
