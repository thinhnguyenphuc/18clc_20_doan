package com.example.photo_manager.ui.SecureFolder;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.R;

import com.example.photo_manager.ui.SecureFolder.SecureFolderViewModel.SecureFolderViewModel;

import com.example.photo_manager.ui.ViewPhotoFragmentDirections;

import java.io.File;


public class ViewSFPhotoFragment extends Fragment {

    private SecureFolderViewModel secureFolderViewModel;

    private NavController navController;

    private Toolbar toolbar_top;
    private Toolbar toolbar_bottom;

    private File file;

    private Picture_Model picture_model = new Picture_Model(null,null,null,0);
    private SubsamplingScaleImageView imageView;
    private ImageButton edit_button, delete_button;

    Bitmap bitmap;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sf_view_photo, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().findViewById(R.id.nav_view).setVisibility(View.INVISIBLE);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());

        String filePath = ViewSFPhotoFragmentArgs.fromBundle(getArguments()).getFilePath();

        this.file = new File(filePath);

        navController = Navigation.findNavController(view);

        secureFolderViewModel = viewModelProvider.get(SecureFolderViewModel.class);

        imageView = (SubsamplingScaleImageView) view.findViewById(R.id.imageView);

        Glide.with(this)
                .asBitmap()
                .load(file)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        bitmap = resource;
                        imageView.setImage(ImageSource.bitmap(resource));
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });


        toolbar_top = view.findViewById(R.id.toolbar_top);
        toolbar_bottom = view.findViewById(R.id.toolbar_bottom);

        toolbar_top.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.vp_menu_detail:{
                        break;
                    }
                }
                return true;
            }
        });

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
                navController.popBackStack();
            }
        });

        this.edit_button = view.findViewById(R.id.edit_button);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = ViewSFPhotoFragmentDirections.actionViewSFPhotoFragmentToSFEditPhotoFragment(filePath);
                navController.navigate(action);
            }
        });


        view.findViewById(R.id.delete_button).setOnClickListener(v -> {
            if (deleteImage()) {
                Toast.makeText(requireContext(), R.string.delete_image_success, Toast.LENGTH_LONG).show();
                navController.popBackStack();
            } else {
                Toast.makeText(requireContext(), R.string.delete_image_fail, Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean deleteImage() {
        return secureFolderViewModel.delete(file);
    }

    private void setWallpaper() {
        Context context = requireContext();
        String title = getString(R.string.set_wallpaper);
        String content = getString(R.string.ask_set_wallpaper);

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(content);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
//                new SetWallperAsynTask(requireContext()).execute(Uri.parse(photo_uri));
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private class SetWallperAsynTask extends AsyncTask<Uri, Void, Boolean> {
        private Context context;

        public SetWallperAsynTask(Context context) {
            this.context = context;
        }
        @Override
        protected Boolean doInBackground(Uri... uris) {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
            try {
                if (uris.length == 1) {
                    if(  uris[0] != null   ){
                        wallpaperManager.setBitmap(bitmap);
                        return true;
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                Toast.makeText(requireContext(), R.string.set_wallpaper_success, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireContext(), R.string.set_wallpaper_fail, Toast.LENGTH_LONG).show();
            }
        }
    }

}
