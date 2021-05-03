package com.example.photo_manager.ui.Album;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.photo_manager.ui.Album.AlbumDatabase.Album.Album;
import com.example.photo_manager.ui.Album.AlbumDatabase.Album.AlbumReposistory;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumUri.AlbumUri;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumUri.AlbumUriReposistory;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumWithUris;

import java.util.List;

public class AlbumViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    LiveData<List<AlbumWithUris>> albumsWithUris;
    AlbumReposistory albumReposistory;
    AlbumUriReposistory albumUriReposistory;

    public AlbumViewModel(@NonNull Application application) {
        super(application);
        albumReposistory = new AlbumReposistory(application);
        albumUriReposistory = new AlbumUriReposistory(application);
        albumsWithUris = albumReposistory.getAllAlbumsWithUris();
    }

    public LiveData<List<AlbumWithUris>> getAlbumsWithUris() {
        return albumsWithUris;
    }

    public LiveData<AlbumWithUris> getAlbumWithUris(int albumId) {
        return albumReposistory.getAlbumWithUris(albumId);
    }

    public void insertAlbum(Album... albums) {
        albumReposistory.insert(albums);
    }

    public void deleteAlbum(Album... albums) {
        albumReposistory.delete(albums);
    }

    public void insertAlbumUri(AlbumUri... albumUris) {
        albumUriReposistory.insert(albumUris);
    }

    public void deleteAlbumUri(AlbumUri... albumUris) {
        albumUriReposistory.delete(albumUris);
    }
}