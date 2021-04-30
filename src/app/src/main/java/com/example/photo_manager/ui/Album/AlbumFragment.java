package com.example.photo_manager.ui.Album;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.photo_manager.R;

public class AlbumFragment extends Fragment {

    private AlbumViewModel mViewModel;

    public static AlbumFragment newInstance() {
        return new AlbumFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.album_fragment, container, false);

        mViewModel = new ViewModelProvider(requireActivity()).get(AlbumViewModel.class);

        return root;
    }


}