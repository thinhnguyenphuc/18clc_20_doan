package com.example.photo_manager.ui.Media;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.example.photo_manager.Adapter.Picture_Adapter_All;
import com.example.photo_manager.Code.ResultCode;
import com.example.photo_manager.Model.Date_Model;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.RecyclerViewClickInterface;
import com.example.photo_manager.Code.RequestCode;
import com.example.photo_manager.Take_New_Photo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MediaFragment extends Fragment implements RecyclerViewClickInterface {

    private MediaViewModel mediaViewModel;


    ArrayList<Picture_Model> pictureModels = new ArrayList<>();

    private RecyclerView recyclerView;
    private Picture_Adapter_All picture_adapter_all;

    NavController navController = null;

    Toolbar toolbar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_media, container, false);

        toolbar = root.findViewById(R.id.toolbar_top);

        navController = NavHostFragment.findNavController(this);

        BottomNavigationView bnv = requireActivity().findViewById(R.id.nav_view);
        bnv.setVisibility(View.VISIBLE);

        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.favourite:
                                navController.navigate(R.id.action_mediaFragment_to_favouriteFragment);
                                break;
                            case R.id.camera:
                                startActivityForResult(new Intent(requireActivity(), Take_New_Photo.class), RequestCode.REQUEST_INTENT_TAKE_NEW_PHOTO);
                                break;
                            case R.id.slideshow:
                                MediaFragmentDirections.ActionMediaFragmentToSlideShowFragment action
                                        = MediaFragmentDirections.actionMediaFragmentToSlideShowFragment();
                                navController.navigate(action);
                        }
                        return true;
                    }
                });

        recyclerView = root.findViewById(R.id.recyclerView_ViewAll);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));

        picture_adapter_all = new Picture_Adapter_All(getContext(),this);
        recyclerView.setAdapter(picture_adapter_all);

        mediaViewModel =
                new ViewModelProvider(requireActivity()).get(MediaViewModel.class);
        final SkeletonScreen skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(picture_adapter_all)
                .shimmer(true)
                .angle(20)
                .frozen(false)
                .duration(1200)
                .count(40)
                .load(R.layout.item_skeleton_news)
                .show(); //default count is 10

        mediaViewModel.getAllPictures().observe(getViewLifecycleOwner(), new Observer<ArrayList<Picture_Model>>() {
            @Override
            public void onChanged(ArrayList<Picture_Model> picture_models) {
                if(picture_models.size()>0){
                    skeletonScreen.hide();
                }
                MediaFragment.this.pictureModels = picture_models;
                picture_adapter_all.setPictures(picture_models);
            }

        });
        mediaViewModel.getAllDates().observe(getViewLifecycleOwner(), new Observer<ArrayList<Date_Model>>() {
            @Override
            public void onChanged(ArrayList<Date_Model> date_models) {

            }
        });
        return root;
    }

    @Override
    public void onItemClick(int position) {

//        Picture_Model picture_model = pictureModels.get(position);
//
//        Intent view_photo = new Intent(getActivity(), ViewPhoto.class);
//        view_photo.putExtra("uri",picture_model.getUri().toString());
//        view_photo.putExtra("name",picture_model.getName());
//        view_photo.putExtra("time",picture_model.getTime());
//        view_photo.putExtra("size",picture_model.getSize());
//        startActivityForResult(view_photo, RequestCode.REQUEST_INTENT_VIEW_PHOTO);
        MediaFragmentDirections.ActionMediaFragmentToViewPhotoFragment action =
                MediaFragmentDirections.actionMediaFragmentToViewPhotoFragment(pictureModels.get(position).getUri().toString());
        navController.navigate(action);
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
                mediaViewModel.delete(Uri.parse(data.getStringExtra("uri")));
            }else if (resultCode == ResultCode.RESULT_VIEW_PHOTO_EDITED) {
                Log.d("my debugger", "on fragment result result: ");
                recyclerView.setAdapter(picture_adapter_all);
            }
        } else if (requestCode == RequestCode.REQUEST_INTENT_TAKE_NEW_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                mediaViewModel.update(requireActivity());
            }
        }
    }


}