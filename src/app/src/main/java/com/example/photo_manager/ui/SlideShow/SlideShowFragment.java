package com.example.photo_manager.ui.SlideShow;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumUri.AlbumUri;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumWithUris;
import com.example.photo_manager.ui.Album.AlbumViewModel;
import com.example.photo_manager.ui.Favourite.FavouriteDababase.FavouriteItem;
import com.example.photo_manager.ui.Favourite.FavouriteViewModel;
import com.example.photo_manager.ui.Picture.PictureViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class SlideShowFragment extends Fragment {

    SliderView sliderView;
    SlideShowAdapter slideShowAdapter;
    Toolbar toolbar;

    Toast toastPause, toastPlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_slide_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sliderView = view.findViewById(R.id.imageSlider);
        toolbar = view.findViewById(R.id.toolbar_top);
;
        slideShowAdapter = new SlideShowAdapter(getContext(), toolbar);

        BottomNavigationView bnv = requireActivity().findViewById(R.id.nav_view);
        bnv.setVisibility(View.INVISIBLE);

        sliderView.setSliderAdapter(slideShowAdapter);

        setUpSliderView();
        setUpData();

        toastPause = Toast.makeText(requireContext(), R.string.pause, Toast.LENGTH_SHORT);
        toastPlay = Toast.makeText(requireContext(), R.string.play, Toast.LENGTH_SHORT);

        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.pause: {
                    sliderView.stopAutoCycle();
                    toastPlay.cancel();
                    toastPause.setGravity(Gravity.CENTER, 0, 0);
                    toastPause.show();
                    break;
                }
                case R.id.play: {
                    sliderView.startAutoCycle();
                    toastPause.cancel();
                    toastPlay.setGravity(Gravity.CENTER, 0, 0);
                    toastPlay.show();
                    break;
                }
            }
            return true;
        });

        view.findViewById(R.id.back_button).setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
    }

    private void setUpSliderView() {

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setAutoCycle(true);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }

    private void setUpData() {

        //-1 is all, 0 is favourite
        int albumId = SlideShowFragmentArgs.fromBundle(getArguments()).getAlbumId();

        switch (albumId) {
            case -1:
            {
                Log.d("SLIDE SHOW", "setUpData: " + albumId);
                PictureViewModel viewModel = new ViewModelProvider(requireActivity()).get(PictureViewModel.class);
                viewModel.getAllPictures().observe(getViewLifecycleOwner(), new Observer<ArrayList<Picture_Model>>() {
                    @Override
                    public void onChanged(ArrayList<Picture_Model> picture_models) {
                        List<String> uris = getUriFromPictureModels(picture_models);
                        slideShowAdapter.renewItems(uris);
                    }
                });
                break;
            }
            case 0:
            {Log.d("SLIDE SHOW", "setUpData: " + albumId);
                FavouriteViewModel viewModel = new ViewModelProvider(requireActivity()).get(FavouriteViewModel.class);
                viewModel.getAllFavouriteItems().observe(getViewLifecycleOwner(), new Observer<List<FavouriteItem>>() {
                    @Override
                    public void onChanged(List<FavouriteItem> favouriteItems) {
                        List<String> uris = getUriFromFavouriteItems(favouriteItems);
                        slideShowAdapter.renewItems(uris);
                    }
                });
                break;
            }
            default:
            {Log.d("SLIDE SHOW", "setUpData: " + albumId);
                AlbumViewModel viewModel = new ViewModelProvider(requireActivity()).get(AlbumViewModel.class);
                viewModel.getAlbumWithUris(albumId).observe(getViewLifecycleOwner(), new Observer<AlbumWithUris>() {
                    @Override
                    public void onChanged(AlbumWithUris albumWithUris) {
                        List<String> uris = getUriFromAlbumUris(albumWithUris.albumUris);
                        slideShowAdapter.renewItems(uris);
                    }
                });
                break;
            }
        }
    }

    List<String> getUriFromPictureModels(List<Picture_Model> picture_models) {
        List<String> uris = new ArrayList<>();

        for (Picture_Model picture_model: picture_models) {
            uris.add(picture_model.getUri().toString());
        }

        return uris;
    }

    List<String> getUriFromAlbumUris(List<AlbumUri> albumUris) {
        List<String> uris = new ArrayList<>();

        for (AlbumUri albumUri: albumUris) {
            uris.add(albumUri.getUri());
        }

        return uris;
    }

    List<String> getUriFromFavouriteItems(List<FavouriteItem> favouriteItems) {
        List<String> uris = new ArrayList<>();

        for (FavouriteItem favouriteItem: favouriteItems) {
            uris.add(favouriteItem.getUri());
        }

        return uris;
    }
}