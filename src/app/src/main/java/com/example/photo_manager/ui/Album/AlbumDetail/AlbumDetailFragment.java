package com.example.photo_manager.ui.Album.AlbumDetail;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anggrayudi.storage.media.MediaFile;
import com.example.photo_manager.R;
import com.example.photo_manager.Utility;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumUri.AlbumUri;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumWithUris;
import com.example.photo_manager.ui.Album.AlbumViewModel;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetailFragment extends Fragment {

    AlbumViewModel albumViewModel;
    NavController navController;

    int albumId;

    Toolbar toolbar;

    AlbumDetailAdapter adapter;

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

        adapter = new AlbumDetailAdapter(getContext(), navController, albumId);

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        recyclerView.setLayoutManager(flexboxLayoutManager);
        recyclerView.setAdapter(adapter);

        albumViewModel.getAlbumWithUris(albumId).observe(getViewLifecycleOwner(), new Observer<AlbumWithUris>() {
            @Override
            public void onChanged(AlbumWithUris albumWithUris) {
                new ValidateUriAsynTask(getContext()).execute(albumWithUris.albumUris.toArray(new AlbumUri[0]));
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
                case R.id.add_photo: {
                    AlbumDetailFragmentDirections.ActionAlbumDetailFragmentToAddMediaFragment action =
                            AlbumDetailFragmentDirections.actionAlbumDetailFragmentToAddMediaFragment(albumId);
                    navController.navigate(action);
                    break;
                }
                case R.id.add_video: {
                    NavDirections action =
                            AlbumDetailFragmentDirections.actionAlbumDetailFragmentToAddVideoFragment(albumId);
                    navController.navigate(action);
                    break;
                }
                case R.id.delete_album: {
                    albumViewModel.deleteAlbumWhereIdEqual(albumId);
                    Toast.makeText(requireContext(), R.string.delete_album_success, Toast.LENGTH_LONG).show();
                    navController.popBackStack();
                    break;
                }
                case R.id.slideshow: {
                    AlbumDetailFragmentDirections.ActionAlbumDetailFragmentToSlideShowFragment action =
                            AlbumDetailFragmentDirections.actionAlbumDetailFragmentToSlideShowFragment(albumId);
                    navController.navigate(action);
                }
            }
            return true;
        });

    }

    private class ValidateUriAsynTask extends AsyncTask<AlbumUri, Void, Void> {

        Context context;

        public ValidateUriAsynTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(AlbumUri... albumUris) {
            List<AlbumUri> albumUriList = new ArrayList<>();
            for (AlbumUri albumUri: albumUris) {
                if (!validateUri(albumUri.getUri())) {
                    albumUriList.add(albumUri);
                    Log.d("DEBUG", "validateUri: " + false);
                }
            }

            albumViewModel.deleteAlbumUri(albumUriList.toArray(new AlbumUri[0]));
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
        albumViewModel.getAlbumWithUris(albumId).removeObservers(getViewLifecycleOwner());
        albumViewModel.getAlbumWithUris(albumId).observe(getViewLifecycleOwner(), new Observer<AlbumWithUris>() {
            @Override
            public void onChanged(AlbumWithUris albumWithUris) {
                new ValidateUriAsynTask(getContext()).execute(albumWithUris.albumUris.toArray(new AlbumUri[0]));
                adapter.setData(albumWithUris);
            }

        });
    }
}