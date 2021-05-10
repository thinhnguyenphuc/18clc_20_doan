package com.example.photo_manager.ui.Album;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.photo_manager.Utility;
import com.example.photo_manager.R;
import com.example.photo_manager.ui.Album.AlbumDatabase.Album.Album;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumWithUris;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class AlbumFragment extends Fragment {

    private AlbumViewModel albumViewModel;

    private AlbumAdapter albumAdapter;

    private RecyclerView recyclerView;

    private NavController navController;

    public static AlbumFragment newInstance() {
        return new AlbumFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_album, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bnv = requireActivity().findViewById(R.id.nav_view);
        bnv.setVisibility(View.VISIBLE);

        albumViewModel = new ViewModelProvider(requireActivity()).get(AlbumViewModel.class);
        navController = Navigation.findNavController(view);

        recyclerView = view.findViewById(R.id.album_list);
        albumAdapter = new AlbumAdapter(getContext(), navController);
        recyclerView.setAdapter(albumAdapter);

        int dp = (int) (getResources().getDimension(R.dimen.picture_width) / getResources().getDisplayMetrics().density);

        GridLayoutManager layoutManager =
                new GridLayoutManager(getContext(), Utility.calculateNoOfColumns(getContext(), dp));
        recyclerView.setLayoutManager(layoutManager);

        albumViewModel.getAlbumsWithUris().observe(getViewLifecycleOwner(), new Observer<List<AlbumWithUris>>() {
            @Override
            public void onChanged(List<AlbumWithUris> albumsWithPhotoUris) {
                for (AlbumWithUris albumWithUris: albumsWithPhotoUris) {
                    Log.d("ALBUM FRAGMENT", "onChanged: " + albumWithUris.album.getId() + "\n");
                }
                albumAdapter.setData(albumsWithPhotoUris);
            }
        });

        Toolbar toolbar_top = view.findViewById(R.id.toolbar_top);
        toolbar_top.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.create_album:
                        createAlbum();
                        break;
                }
                return true;
            }
        });
    }

    private void createAlbum() {
        AlertDialog create_album_dialog = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_create_with_title, null);
        EditText title = view.findViewById(R.id.title);
        TextView error = view.findViewById(R.id.error);

        create_album_dialog.setView(view);
        create_album_dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (title.getText().length() == 0) {
                    error.setText(getString(R.string.album_name_empty_error));
                } else {
                    error.setText("");
                    albumViewModel.insertAlbum(new Album(title.getText().toString()));
                }
            }
        });
        create_album_dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.Cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                create_album_dialog.cancel();
            }
        });

        create_album_dialog.show();
    }


}