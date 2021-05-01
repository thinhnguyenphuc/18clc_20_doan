package com.example.photo_manager.ui.Favourite;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.example.photo_manager.PEAdapters.Utility;
import com.example.photo_manager.R;
import com.example.photo_manager.RecyclerViewClickInterface;
import com.example.photo_manager.ui.Favourite.FavouriteDababase.FavouriteItem;
import com.example.photo_manager.ui.Media.MediaFragmentDirections;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class FavouriteFragment extends Fragment implements RecyclerViewClickInterface {

    private FavouriteViewModel favouriteViewModel;

    public static FavouriteFragment newInstance() {
        return new FavouriteFragment();
    }

    NavController navController;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        requireActivity().findViewById(R.id.nav_view).setVisibility(View.INVISIBLE);

        RecyclerView recyclerView = view.findViewById(R.id.favourite_item_list);

        FavouriteAdapter adapter = new FavouriteAdapter(getContext(), navController);

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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.favourite_fragment, container, false);

        return root;

    }


    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onLongItemClick(int position) {

    }
}