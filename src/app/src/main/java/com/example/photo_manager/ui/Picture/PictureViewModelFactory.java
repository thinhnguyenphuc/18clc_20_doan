package com.example.photo_manager.ui.Picture;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PictureViewModelFactory implements ViewModelProvider.Factory {
    private Context context;



    public PictureViewModelFactory(Context context) {
        this.context = context;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new PictureViewModel(context);
    }
}
