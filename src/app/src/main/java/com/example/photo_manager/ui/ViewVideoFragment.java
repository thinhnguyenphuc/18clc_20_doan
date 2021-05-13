package com.example.photo_manager.ui;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.photo_manager.Model.Video_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.Utility;
import com.example.photo_manager.ui.Favourite.FavouriteDababase.FavouriteItem;
import com.example.photo_manager.ui.Favourite.FavouriteViewModel;
import com.example.photo_manager.ui.Video.VideoViewModel;
import com.github.rtoshiro.view.video.FullscreenVideoLayout;

import java.io.IOException;

public class ViewVideoFragment extends Fragment {


    private NavController navController;
    private VideoViewModel videoViewModel ;
    private String video_uri;
    private Video_Model video_model = new Video_Model(null,null,null,0,0);
    private FullscreenVideoLayout videoView;
    boolean current_favourite_flag = false;
    private Toolbar toolbar_top;
    boolean favourite_flag;
    MenuItem favourite_button;

    private FavouriteViewModel favouriteViewModel;

    FavouriteCheckAsyncTask favouriteCheckAsyncTask;


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
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().findViewById(R.id.nav_view).setVisibility(View.INVISIBLE);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());

        navController = Navigation.findNavController(view);

        videoViewModel = viewModelProvider.get(VideoViewModel.class);

        video_uri = ViewVideoFragmentArgs.fromBundle(getArguments()).getVideoUri();

        video_model.setUri(Uri.parse(video_uri));

        videoView = (FullscreenVideoLayout) view.findViewById(R.id.videoView);

        favouriteViewModel = viewModelProvider.get(FavouriteViewModel.class);



        this.favouriteCheckAsyncTask = new ViewVideoFragment.FavouriteCheckAsyncTask(favouriteViewModel);
        this.favouriteCheckAsyncTask.execute();

        try {
            videoView.setVideoURI(video_model.getUri());
        } catch (IOException e) {
            e.printStackTrace();
        }

        view.findViewById(R.id.video_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });


        toolbar_top = view.findViewById(R.id.video_toolbar_top);

        favourite_button = toolbar_top.getMenu().findItem(R.id.video_favourite);

        toolbar_top.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.video_detail:{
                        ViewVideoFragmentDirections.ActionViewVideoToVideoDetailFragment action =
                                ViewVideoFragmentDirections.actionViewVideoToVideoDetailFragment(video_uri);
                        navController.navigate(action);
                        break;
                    }
                    case R.id.video_delete:
                    {
                        if (deleteVideo(Uri.parse(video_uri))) {
                            Toast.makeText(requireContext(), R.string.delete_video_success, Toast.LENGTH_LONG).show();
                            videoViewModel.delete(video_model.getUri());
                            NavHostFragment.findNavController(ViewVideoFragment.this).popBackStack();
                        } else {
                            Toast.makeText(requireContext(), R.string.delete_video_fail, Toast.LENGTH_LONG).show();
                        }
                        break;
                    }
                    case R.id.video_secure_folder:
                    {
                        break;
                    }
                    case R.id.video_favourite:
                    {
                        if (favouriteCheckAsyncTask != null && favouriteCheckAsyncTask.getStatus() != AsyncTask.Status.FINISHED) {
                            Toast.makeText(getContext(), R.string.loading, Toast.LENGTH_LONG).show();
                        }
                        else if (favouriteCheckAsyncTask != null && favouriteCheckAsyncTask.getStatus() == AsyncTask.Status.FINISHED){
                            if (!current_favourite_flag) {
                                item.setIcon(R.drawable.red_heart_icon);
                                current_favourite_flag = true;
                            } else {
                                item.setIcon(R.drawable.favourite_icon);
                                current_favourite_flag = false;
                            }
                        }
                        break;
                    }
                }
                return true;
            }
        });
    }
    private boolean deleteVideo(Uri uri) {
        return videoViewModel.deleteFromDevice(requireContext(), uri);
    }

    private class FavouriteCheckAsyncTask extends AsyncTask<String, Void, Boolean> {

        FavouriteViewModel favouriteViewModel;

        public FavouriteCheckAsyncTask(FavouriteViewModel favouriteViewModel) {
            this.favouriteViewModel = favouriteViewModel;
        }
        @Override
        protected Boolean doInBackground(String... strings) {
            return favouriteViewModel.checkUriExistence(video_uri);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            current_favourite_flag = favourite_flag = aBoolean;
            if (favourite_flag) {
               favourite_button.setIcon(R.drawable.red_heart_icon);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (favouriteCheckAsyncTask != null && favouriteCheckAsyncTask.getStatus() != AsyncTask.Status.FINISHED)
            favouriteCheckAsyncTask.cancel(true);
        if (current_favourite_flag) {
            favouriteViewModel.insert(new FavouriteItem(video_uri, 1));
        }
        else if (favouriteCheckAsyncTask != null && favouriteCheckAsyncTask.getStatus() == AsyncTask.Status.FINISHED){
            if (current_favourite_flag != favourite_flag) {
                if (!current_favourite_flag) {
                    favouriteViewModel.delete(new FavouriteItem(video_uri, 1));
                } else {
                    favouriteViewModel.insert(new FavouriteItem(video_uri, 1));
                }
            }
        }
    }
}
