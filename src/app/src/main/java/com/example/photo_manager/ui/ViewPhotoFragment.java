package com.example.photo_manager.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.photo_manager.Code.ResultCode;
import com.example.photo_manager.PEAdapters.Utility;
import com.example.photo_manager.PhotoEditActivity;
import com.example.photo_manager.Picture_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.ViewPhoto;
import com.example.photo_manager.ui.Favourite.FavouriteViewModel;
import com.example.photo_manager.ui.Media.MediaViewModel;

public class ViewPhotoFragment extends Fragment {
    private MediaViewModel mediaViewModel;
    private FavouriteViewModel favouriteViewModel;

    Toolbar toolbar_top;
    Toolbar toolbar_bottom;

    String photo_uri;

    private Picture_Model picture_model = new Picture_Model(null,null,null,0);
    SubsamplingScaleImageView imageView;
    ImageButton favourite_button, edit_button, share_button, delete_button;
    boolean favourite_flag;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_view_photo, container, false);

        requireActivity().findViewById(R.id.nav_view).setVisibility(View.INVISIBLE);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        mediaViewModel = viewModelProvider.get(MediaViewModel.class);
        favouriteViewModel = viewModelProvider.get(FavouriteViewModel.class);

        toolbar_top = root.findViewById(R.id.toolbar_top);
        toolbar_bottom = root.findViewById(R.id.toolbar_bottom);

        photo_uri = ViewPhotoFragmentArgs.fromBundle(getArguments()).getPhotoUri();

        toolbar_top.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                }
                return true;
            }
        });

        picture_model.setUri(Uri.parse(photo_uri));

        imageView = (SubsamplingScaleImageView)root.findViewById(R.id.imageView);

        try {
            imageView.setImage(ImageSource.uri(picture_model.getUri()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Glide.with(this).load(picture_model.getUri()).into(imageView);


        toolbar_top = root.findViewById(R.id.toolbar_top);
        toolbar_bottom = root.findViewById(R.id.toolbar_bottom);

        favourite_button = root.findViewById(R.id.favourite_button);
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

        root.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });


        this.favourite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!favourite_flag) {
                    favourite_button.setImageResource(R.drawable.red_heart_icon);
                    favourite_flag = true;
                } else {
                    favourite_button.setImageResource(R.drawable.favourite_icon);
                    favourite_flag = false;
                }
            }
        });

        this.edit_button = root.findViewById(R.id.edit_button);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPhotoFragmentDirections.ActionViewPhotoFragmentToEditPhotoFragment action =
                        ViewPhotoFragmentDirections.actionViewPhotoFragmentToEditPhotoFragment(photo_uri);
                Navigation.findNavController(v).navigate(action);
            }
        });

        root.findViewById(R.id.delete_button).setOnClickListener(v -> {
            if (deleteImage(picture_model.getUri())) {
                Intent returnData = new Intent();
                returnData.putExtra("uri", picture_model.getUri().toString());
                Toast.makeText(requireContext(), "IMAGE IS DELETED", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireContext(), "FAILED TO DELETE IMAGE", Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    private boolean deleteImage(Uri uri) {
        return true;
    }
}
