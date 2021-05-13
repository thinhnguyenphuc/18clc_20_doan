package com.example.photo_manager.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.photo_manager.Format.FormatDate;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.Utility;

import java.io.File;

public class PhotoDetailFragment extends Fragment {

    String photo_uri;
    Picture_Model picture_model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.photo_uri = PhotoDetailFragmentArgs.fromBundle(getArguments()).getPhotoUri();

        picture_model = new Picture_Model(null, null, null, 0);

        picture_model.setUri(Uri.parse(photo_uri));

        String photo_path = Utility.getRealPathFromUri(getContext(), picture_model.getUri());

        File file = new File(photo_path);

        picture_model.setName(file.getName());
        picture_model.setTime(FormatDate.fullFormat.format(file.lastModified()));
        picture_model.setSize((int) file.length());


        ImageView imageView_detail = (ImageView) view.findViewById(R.id.imageView_details);
        TextView textView_path = (TextView) view.findViewById(R.id.textView_path);
        TextView textView_resolution = (TextView) view.findViewById(R.id.textView_resolution);
        TextView textView_size = (TextView) view.findViewById(R.id.textView_size);
        TextView textView_time = (TextView) view.findViewById(R.id.textView_time);

        textView_size.setText(String.valueOf(picture_model.getSize()/1024)+"KB");

        Glide.with(this).load(picture_model.getUri()).into(imageView_detail);

        textView_time.setText(picture_model.getTime());

        textView_path.setText(photo_path);
    }

}