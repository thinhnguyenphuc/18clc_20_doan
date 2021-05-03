package com.example.photo_manager.ui.Album.AlbumDetail;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.Utility;
import com.example.photo_manager.R;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumUri.AlbumUri;
import com.example.photo_manager.ui.Album.AlbumViewModel;
import com.example.photo_manager.ui.Favourite.FavouriteDababase.FavouriteItem;
import com.example.photo_manager.ui.Favourite.FavouriteViewModel;
import com.example.photo_manager.ui.Media.MediaViewModel;


public class ViewAlbumPhotoFragment extends Fragment {


    private MediaViewModel mediaViewModel;
    private FavouriteViewModel favouriteViewModel;
    private AlbumViewModel albumViewModel;

    NavController navController;

    Toolbar toolbar_top;
    Toolbar toolbar_bottom;

    //Safe args
    String photo_uri;
    int albumId;

    private Picture_Model picture_model = new Picture_Model(null,null,null,0);
    SubsamplingScaleImageView imageView;
    ImageButton favourite_button, edit_button, share_button, delete_button;
    boolean favourite_flag;
    boolean current_favourite_flag = false;

    FavouriteCheckAsyncTask favouriteCheckAsyncTask;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view_album_photo, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().findViewById(R.id.nav_view).setVisibility(View.INVISIBLE);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());

        navController = Navigation.findNavController(view);

        mediaViewModel = viewModelProvider.get(MediaViewModel.class);
        favouriteViewModel = viewModelProvider.get(FavouriteViewModel.class);
        albumViewModel = viewModelProvider.get(AlbumViewModel.class);

        photo_uri = ViewAlbumPhotoFragmentArgs.fromBundle(getArguments()).getPhotoUri();
        albumId = ViewAlbumPhotoFragmentArgs.fromBundle(getArguments()).getAlbumId();

        this.favouriteCheckAsyncTask = new FavouriteCheckAsyncTask(favouriteViewModel);
        this.favouriteCheckAsyncTask.execute();

        picture_model.setUri(Uri.parse(photo_uri));

        imageView = (SubsamplingScaleImageView)view.findViewById(R.id.imageView);

        try {
            imageView.setImage(ImageSource.uri(picture_model.getUri()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Glide.with(this).load(picture_model.getUri()).into(imageView);

        toolbar_top = view.findViewById(R.id.toolbar_top);
        toolbar_bottom = view.findViewById(R.id.toolbar_bottom);

        toolbar_top.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.vp_menu_photo_detail:{
                        ViewAlbumPhotoFragmentDirections.ActionViewAlbumPhotoFragmentToPhotoDetailFragment action =
                                ViewAlbumPhotoFragmentDirections.actionViewAlbumPhotoFragmentToPhotoDetailFragment(photo_uri);
                        navController.navigate(action);
                        break;
                    }
                }
                return true;
            }
        });

        favourite_button = view.findViewById(R.id.favourite_button);
        favourite_flag = Utility.checkImageIsFavourite("");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toolbar_top.getVisibility() == View.INVISIBLE) {
                    toolbar_top.setVisibility(View.VISIBLE);
                } else {
                    toolbar_top.setVisibility(View.INVISIBLE);
                }

                if (toolbar_bottom.getVisibility() == View.INVISIBLE) {
                    toolbar_bottom.setVisibility(View.VISIBLE);
                } else {
                    toolbar_bottom.setVisibility(View.INVISIBLE);
                }
            }
        });

        view.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });


        this.favourite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favouriteCheckAsyncTask != null && favouriteCheckAsyncTask.getStatus() != AsyncTask.Status.FINISHED) {
                    Toast.makeText(getContext(), R.string.loading, Toast.LENGTH_LONG).show();
                }
                else if (favouriteCheckAsyncTask != null && favouriteCheckAsyncTask.getStatus() == AsyncTask.Status.FINISHED){
                    if (!current_favourite_flag) {
                        favourite_button.setImageResource(R.drawable.red_heart_icon);
                        current_favourite_flag = true;
                    } else {
                        favourite_button.setImageResource(R.drawable.favourite_icon);
                        current_favourite_flag = false;
                    }
                }
            }
        });

        this.edit_button = view.findViewById(R.id.edit_button);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewAlbumPhotoFragmentDirections.ActionViewAlbumPhotoFragmentToEditPhotoFragment action =
                        ViewAlbumPhotoFragmentDirections.actionViewAlbumPhotoFragmentToEditPhotoFragment(photo_uri);
                Navigation.findNavController(v).navigate(action);
            }
        });

        view.findViewById(R.id.delete_button).setOnClickListener(v -> {
           albumViewModel.deleteAlbumUri(new AlbumUri(photo_uri, albumId, 0));
           Toast.makeText(getContext(), R.string.remove_from_album_success, Toast.LENGTH_LONG).show();
           navController.popBackStack();
        });
    }


    @Override
    public void onPause() {
        super.onPause();

        if (favouriteCheckAsyncTask != null && favouriteCheckAsyncTask.getStatus() != AsyncTask.Status.FINISHED)
            favouriteCheckAsyncTask.cancel(true);
        if (current_favourite_flag) {
            favouriteViewModel.insert(new FavouriteItem(photo_uri, 0));
        }
        else if (favouriteCheckAsyncTask != null && favouriteCheckAsyncTask.getStatus() == AsyncTask.Status.FINISHED){
            if (current_favourite_flag != favourite_flag) {
                if (!current_favourite_flag) {
                    favouriteViewModel.delete(new FavouriteItem(photo_uri, 0));
                } else {
                    favouriteViewModel.insert(new FavouriteItem(photo_uri, 0));
                }
            }
        }


    }

    private class FavouriteCheckAsyncTask extends AsyncTask<String, Void, Boolean> {

        FavouriteViewModel favouriteViewModel;

        public FavouriteCheckAsyncTask(FavouriteViewModel favouriteViewModel) {
            this.favouriteViewModel = favouriteViewModel;
        }
        @Override
        protected Boolean doInBackground(String... strings) {
            return favouriteViewModel.checkUriExistence(photo_uri);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            current_favourite_flag = favourite_flag = aBoolean;
            if (favourite_flag) {
                favourite_button.setImageResource(R.drawable.red_heart_icon);
            }
        }
    }
}