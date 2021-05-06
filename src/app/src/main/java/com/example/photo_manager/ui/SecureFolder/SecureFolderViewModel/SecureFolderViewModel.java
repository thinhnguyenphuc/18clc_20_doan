package com.example.photo_manager.ui.SecureFolder.SecureFolderViewModel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.util.List;


public class SecureFolderViewModel extends AndroidViewModel {

    MutableLiveData<List<File>> filePaths;
    SecureFolderReposistory reposistory;

    public SecureFolderViewModel(@NonNull  Application application) {
        super(application);
        reposistory = new SecureFolderReposistory(application);
        filePaths = reposistory.getFiles();
    }

    public boolean addBitmapToFolder(Context context, Bitmap bitmap) {
        return reposistory.addBitmapToFolder(context, bitmap);
    }

    public MutableLiveData<List<File>> getFiles() {
        return reposistory.getFiles();
    }

    public boolean delete(File file) {
        return reposistory.delete(file);
    }

    public boolean resave(Context context, Bitmap bitmap, String fileName) {
        return reposistory.resave(context, bitmap, fileName);
    }

    public void deleteListOfFile(List<File> files) {
        reposistory.deleteListOfFile(files);
    }

}
