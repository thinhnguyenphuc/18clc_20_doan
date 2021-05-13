package com.example.photo_manager.ui.Favourite;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anggrayudi.storage.media.MediaFile;
import com.example.photo_manager.R;
import com.example.photo_manager.RecyclerViewClickInterface;
import com.example.photo_manager.Utility;
import com.example.photo_manager.ui.Favourite.FavouriteDababase.FavouriteItem;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment implements RecyclerViewClickInterface {

    private FavouriteViewModel favouriteViewModel;

    public static FavouriteFragment newInstance() {
        return new FavouriteFragment();
    }

    NavController navController;

    Toolbar toolbar;

    FavouriteAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        requireActivity().findViewById(R.id.nav_view).setVisibility(View.INVISIBLE);

        RecyclerView recyclerView = view.findViewById(R.id.favourite_item_list);

        adapter = new FavouriteAdapter(getContext(), navController);

        int picture_width = (int) (getResources().getDimension(R.dimen.picture_width) / getResources().getDisplayMetrics().density);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), Utility.calculateNoOfColumns(getContext(), picture_width));

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        favouriteViewModel = new ViewModelProvider(requireActivity()).get(FavouriteViewModel.class);

        favouriteViewModel.getAllFavouriteItems().observe(getViewLifecycleOwner(), new Observer<List<FavouriteItem>> () {
            @Override
            public void onChanged(List<FavouriteItem> favouriteItemList) {
                adapter.setItems(favouriteItemList);
            }
        });

        view.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });

        toolbar = view.findViewById(R.id.toolbar_top);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.slideshow: {
                    FavouriteFragmentDirections.ActionFavouriteFragmentToSlideShowFragment action =
                            FavouriteFragmentDirections.actionFavouriteFragmentToSlideShowFragment(0);
                    navController.navigate(action);
                    break;
                }
            }
            return true;
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favourite, container, false);

        return root;

    }

    private class ValidateUriAsynTask extends AsyncTask<FavouriteItem, Void, Void> {

        Context context;

        public ValidateUriAsynTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(FavouriteItem... favouriteItems) {
            ArrayList<FavouriteItem> deleteFavouriteItems = new ArrayList<>();
            for (FavouriteItem favouriteItem: favouriteItems) {
                if (!validateUri(favouriteItem.getUri())) {
                    deleteFavouriteItems.add(favouriteItem);
                }
            }
            favouriteViewModel.delete(deleteFavouriteItems.toArray(new FavouriteItem[0]));
            return null;
        }

        private boolean validateUri(String uri) {
            MediaFile file = new MediaFile(context, Uri.parse(uri));
            if (file.getExists()) {
                return true;
            } else {
                return false;
            }
//
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        favouriteViewModel.getAllFavouriteItems().removeObservers(getViewLifecycleOwner());
        favouriteViewModel.getAllFavouriteItems().observe(getViewLifecycleOwner(), new Observer<List<FavouriteItem>>() {
            @Override
            public void onChanged(List<FavouriteItem> favouriteItems) {
                new ValidateUriAsynTask(getContext()).execute(favouriteItems.toArray(new FavouriteItem[0]));
                adapter.setItems(favouriteItems);
            }

        });
    }


    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onLongItemClick(int position) {

    }
}