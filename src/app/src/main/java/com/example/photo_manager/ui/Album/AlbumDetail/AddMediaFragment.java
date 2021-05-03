package com.example.photo_manager.ui.Album.AlbumDetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.davidecirillo.multichoicerecyclerview.MultiChoiceToolbar;
import com.example.photo_manager.MainActivity;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumUri.AlbumUri;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumUri.AlbumUriReposistory;
import com.example.photo_manager.ui.Album.AlbumViewModel;
import com.example.photo_manager.ui.Media.MediaViewModel;

import java.util.ArrayList;


public class AddMediaFragment extends Fragment {

    RecyclerView recyclerView;
    AddMediaAdapter adapter;
    Toolbar toolbar_top;

    CheckBox checkBox;
    int albumId;

    MediaViewModel mediaViewModel;
    AlbumViewModel albumViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_media, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        albumId = AddMediaFragmentArgs.fromBundle(getArguments()).getAlbumId();

        mediaViewModel = new ViewModelProvider(requireActivity()).get(MediaViewModel.class);
        albumViewModel = new ViewModelProvider(requireActivity()).get(AlbumViewModel.class);

        toolbar_top = view.findViewById(R.id.toolbar_top);

        checkBox = view.findViewById(R.id.select_all_check_box);

        recyclerView = view.findViewById(R.id.recyclerView);

        setUpMultiChoiceRecyclerView();

        mediaViewModel.getAllPictures().observe(getViewLifecycleOwner(), new Observer<ArrayList<Picture_Model>>() {
            @Override
            public void onChanged(ArrayList<Picture_Model> picture_models) {
                adapter.setData(picture_models);
            }
        });

        view.findViewById(R.id.back_button).setOnClickListener(v -> {
            Navigation.findNavController(view).popBackStack();
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.selectAll();
                } else {
                    adapter.deselectAll();
                }
            }
        });

        toolbar_top.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.accept_button:
                        adapter.accept();
                        Toast.makeText(getContext(), getString(R.string.add_image_sucess), Toast.LENGTH_LONG).show();
                        Navigation.findNavController(view).popBackStack();
                        break;
                }
                return true;
            }
        });


    }

    private void setUpMultiChoiceRecyclerView() {

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false));

        adapter = new AddMediaAdapter(getContext(), albumViewModel, albumId);

        adapter.setSingleClickMode(true);

        recyclerView.setAdapter(adapter);
    }
}