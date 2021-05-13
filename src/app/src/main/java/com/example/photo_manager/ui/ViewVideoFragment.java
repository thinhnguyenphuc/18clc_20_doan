package com.example.photo_manager.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.photo_manager.Model.Video_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.ui.Video.VideoViewModel;
import com.github.rtoshiro.view.video.FullscreenVideoLayout;

import java.io.IOException;

public class ViewVideoFragment extends Fragment {


    private NavController navController;
    private VideoViewModel videoViewModel ;
    private String video_uri;
    private Video_Model video_model = new Video_Model(null,null,null,0,0);
    private FullscreenVideoLayout videoView;

    private Toolbar toolbar_top;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_video,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        requireActivity().findViewById(R.id.nav_view).setVisibility(View.INVISIBLE);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());

        navController = Navigation.findNavController(view);

        videoViewModel = viewModelProvider.get(VideoViewModel.class);

        video_uri = ViewVideoFragmentArgs.fromBundle(getArguments()).getVideoUri();

        video_model.setUri(Uri.parse(video_uri));

        videoView = (FullscreenVideoLayout) view.findViewById(R.id.videoView);

        try {
            videoView.setVideoURI(video_model.getUri());
        } catch (IOException e) {
            e.printStackTrace();
        }

        toolbar_top = view.findViewById(R.id.toolbar_top);

        toolbar_top.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.vp_menu_detail:{
                        ViewVideoFragmentDirections.ActionViewVideoToVideoDetailFragment action =
                                ViewVideoFragmentDirections.actionViewVideoToVideoDetailFragment(video_uri);
                        navController.navigate(action);
                        break;
                    }
                    case R.id.vp_menu_wallpaper:
                    {
                        break;
                    }
                    case R.id.vp_menu_secure_folder:
                    {
                    }
                }
                return true;
            }
        });
        view.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });
    }


}
