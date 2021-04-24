package com.example.photo_manager.ui.Picture;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photo_manager.Adapter.Picture_Adapter_All;
import com.example.photo_manager.Code.ResultCode;
import com.example.photo_manager.Date_Model;
import com.example.photo_manager.Picture_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.RecyclerViewClickInterface;
import com.example.photo_manager.Code.RequestCode;
import com.example.photo_manager.ViewPhoto;

import java.util.ArrayList;

public class PictureFragment extends Fragment implements RecyclerViewClickInterface {

    private PictureViewModel pictureViewModel;


    ArrayList<Picture_Model> pictureModels = new ArrayList<>();

    private RecyclerView recyclerView;
    private Picture_Adapter_All picture_adapter_all;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.picture_fragment, container, false);

        recyclerView = root.findViewById(R.id.recyclerView_ViewAll);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));

        picture_adapter_all = new Picture_Adapter_All(getContext(),this);
        recyclerView.setAdapter(picture_adapter_all);

        pictureViewModel =
                new ViewModelProvider(requireActivity()).get(PictureViewModel.class);
        pictureViewModel.getAllPictures().observe(getViewLifecycleOwner(), new Observer<ArrayList<Picture_Model>>() {
            @Override
            public void onChanged(ArrayList<Picture_Model> picture_models) {
                //Update RecyclerView
                PictureFragment.this.pictureModels = picture_models;
                picture_adapter_all.setPictures(picture_models);
            }

        });

        pictureViewModel.getAllDates().observe(getViewLifecycleOwner(), new Observer<ArrayList<Date_Model>>() {
            @Override
            public void onChanged(ArrayList<Date_Model> date_models) {

            }
        });
        return root;
    }

    @Override
    public void onItemClick(int position) {

        Picture_Model picture_model = pictureModels.get(position);

        Intent view_photo = new Intent(getActivity(), ViewPhoto.class);
        view_photo.putExtra("uri",picture_model.getUri().toString());
        view_photo.putExtra("name",picture_model.getName());
        view_photo.putExtra("time",picture_model.getTime());
        view_photo.putExtra("size",picture_model.getSize());
        startActivityForResult(view_photo, RequestCode.REQUEST_INTENT_VIEW_PHOTO);
    }

    @Override
    public void onLongItemClick(int position) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("my debugger", "on fragment result: ");
        if (requestCode == RequestCode.REQUEST_INTENT_VIEW_PHOTO) {
            Log.d("my debugger", "on fragment result request: ");
            if (resultCode == ResultCode.RESULT_VIEW_PHOTO_DELETED) {
                assert data != null;
                pictureViewModel.delete(Uri.parse(data.getStringExtra("uri")));
            }else if (resultCode == ResultCode.RESULT_VIEW_PHOTO_EDITED) {
                Log.d("my debugger", "on fragment result result: ");
                recyclerView.setAdapter(picture_adapter_all);
            }
        }
    }
}