package com.example.photo_manager.ui.SecureFolder.SecureFolderViewModel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SecureFolderReposistory {

    MutableLiveData<List<File>> files = new MutableLiveData<>();
    File folder;

    public SecureFolderReposistory(Application application) {
        loadAllFiles(application);
    }

    public MutableLiveData<List<File>> getFiles() {
        File[] files = folder.listFiles();
        assert files != null;
        this.files.setValue(Arrays.asList(files));
        return this.files;
    }

    private void loadAllFiles(Context context) {
        List<String> filePaths = new ArrayList<>();
        folder = context.getFilesDir();
        File[] files = folder.listFiles();
        assert files != null;
        this.files.setValue(Arrays.asList(files));
    }

    public void reload() {
        File[] files = folder.listFiles();
        assert files != null;
        this.files.setValue(Arrays.asList(files));
    }


    public boolean addBitmapToFolder(Context context, Bitmap bitmap) {
        String fileName = UUID.randomUUID().toString() + ".png";
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Log.d("SecureFolderReposistory", "addBitmapToFolder: " + context.getFilesDir());
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(File file) {
        String path = file.getAbsolutePath();
        if (file.delete()) {
            reload();
            return true;
        } else {
            return false;
        }
    }

    public boolean resave(Context context, Bitmap bitmap, String fileName) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void deleteListOfFile(List<File> files) {
        for (File file: files) {
            file.delete();
        }
        reload();
    }
}
