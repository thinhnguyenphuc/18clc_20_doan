package com.example.photo_manager.ui.Video;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.example.photo_manager.Adapter_Picture.Picture_Adapter_All;
import com.example.photo_manager.Adapter_Video.Video_Adapter_All;
import com.example.photo_manager.Code.RequestCode;
import com.example.photo_manager.Model.Date_Model;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.Model.Video_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.RecyclerViewClickInterface;
import com.example.photo_manager.Take_New_Photo;
import com.example.photo_manager.ui.Picture.PictureFragment;
import com.example.photo_manager.ui.Picture.PictureFragmentDirections;
import com.example.photo_manager.ui.Picture.PictureViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class VideoFragment extends Fragment implements RecyclerViewClickInterface {

    private VideoViewModel videoViewModel;

    private ArrayList<Video_Model> videoModels = new ArrayList<>();

    private RecyclerView recyclerView;
    private Video_Adapter_All video_adapter_all;

    NavController navController = null;

    Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.video_fragment, container, false);

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
                                navController.navigate(R.id.action_pictureFragment_to_favouriteFragment);
                                break;
                            case R.id.camera:
                                startActivityForResult(new Intent(requireActivity(), Take_New_Photo.class), RequestCode.REQUEST_INTENT_TAKE_NEW_PHOTO);
                                break;
                        }
                        return true;
                    }
                });

        recyclerView = root.findViewById(R.id.recyclerView_ViewAllVideo);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));

        video_adapter_all = new Video_Adapter_All(getContext(),this);
        recyclerView.setAdapter(video_adapter_all);

        videoViewModel =
                new ViewModelProvider(requireActivity()).get(VideoViewModel.class);
        final SkeletonScreen skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(video_adapter_all)
                .shimmer(true)
                .angle(20)
                .frozen(false)
                .duration(1200)
                .count(40)
                .load(R.layout.item_skeleton_news)
                .show(); //default count is 10

        videoViewModel.getAllVideos().observe(getViewLifecycleOwner(), new Observer<ArrayList<Video_Model>>() {
            @Override
            public void onChanged(ArrayList<Video_Model> video_models) {
                if(video_models.size()>0){
                    skeletonScreen.hide();
                }
                VideoFragment.this.videoModels = video_models;
                video_adapter_all.setVideos(video_models);
            }

        });
        videoViewModel.getAllDates().observe(getViewLifecycleOwner(), new Observer<ArrayList<Date_Model>>() {
            @Override
            public void onChanged(ArrayList<Date_Model> date_models) {

            }
        });
        return root;

    }


    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onLongItemClick(int position) {

    }
}