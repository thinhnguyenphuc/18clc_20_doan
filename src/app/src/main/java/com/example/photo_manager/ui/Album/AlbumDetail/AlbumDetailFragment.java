package com.example.photo_manager.ui.Album.AlbumDetail;

import android.os.Bundle;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.photo_manager.Utility;
import com.example.photo_manager.R;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumWithUris;
import com.example.photo_manager.ui.Album.AlbumViewModel;

public class AlbumDetailFragment extends Fragment {

    AlbumViewModel albumViewModel;
    NavController navController;

    int albumId;

    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        albumViewModel = new ViewModelProvider(requireActivity()).get(AlbumViewModel.class);

        albumId = AlbumDetailFragmentArgs.fromBundle(getArguments()).getAlbumId();

        navController = Navigation.findNavController(view);

        requireActivity().findViewById(R.id.nav_view).setVisibility(View.INVISIBLE);

        RecyclerView recyclerView = view.findViewById(R.id.album_item_list);

        toolbar = view.findViewById(R.id.toolbar_top);

        AlbumDetailAdapter adapter = new AlbumDetailAdapter(getContext(), navController, albumId);

        int picture_width = (int) (getResources().getDimension(R.dimen.picture_width) / getResources().getDisplayMetrics().density);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), Utility.calculateNoOfColumns(getContext(), picture_width));

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        albumViewModel.getAlbumWithUris(albumId).observe(getViewLifecycleOwner(), new Observer<AlbumWithUris>() {
            @Override
            public void onChanged(AlbumWithUris albumWithUris) {
                adapter.setData(albumWithUris);
            }

        });

        view.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });

        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.add_media:
                    AlbumDetailFragmentDirections.ActionAlbumDetailFragmentToAddMediaFragment action =
                            AlbumDetailFragmentDirections.actionAlbumDetailFragmentToAddMediaFragment(albumId);
                    navController.navigate(action);
            }

            return true;
        });
    }
}