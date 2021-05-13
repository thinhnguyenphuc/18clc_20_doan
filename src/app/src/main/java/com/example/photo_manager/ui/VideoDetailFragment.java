package com.example.photo_manager.ui;

import android.media.MediaPlayer;
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
import com.bumptech.glide.request.RequestOptions;
import com.example.photo_manager.Format.FormatDate;
import com.example.photo_manager.Model.Video_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.Utility;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class VideoDetailFragment extends Fragment {
    String video_uri;
    Video_Model video_model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.video_uri = VideoDetailFragmentArgs.fromBundle(getArguments()).getVideoUri();

        video_model = new Video_Model(null, null, null, 0,0);

        video_model.setUri(Uri.parse(video_uri));

        String video_path = Utility.getRealPathFromUri(getContext(), video_model.getUri());

        File file = new File(video_path);

        video_model.setName(file.getName());
        video_model.setTime(FormatDate.fullFormat.format(file.lastModified()));
        video_model.setSize((int) file.length());



        ImageView videoView = (ImageView) view.findViewById(R.id.videoView_detail);
        MediaPlayer mp = MediaPlayer.create(getActivity(),video_model.getUri() );
        video_model.setDuration(mp.getDuration());
        mp.release();

        RequestOptions cropOptions = new RequestOptions().centerCrop();
        Glide.with(getContext())
                .load(video_uri)
                .apply(cropOptions)
                .into(videoView);


        TextView textView_path = (TextView) view.findViewById(R.id.textView_VideoPath);
        TextView textView_size = (TextView) view.findViewById(R.id.textView_VideoSize);
        TextView textView_time = (TextView) view.findViewById(R.id.textView_VideoTime);
        TextView textView_duration = (TextView) view.findViewById(R.id.textView_duration);
        textView_size.setText((video_model.getSize()/1024)+"KB");


        textView_time.setText(video_model.getTime());

        textView_duration.setText(convertDuration(video_model.getDuration()));

        textView_path.setText(video_path);
    }
    private String convertDuration(long duration){
        return String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
    }
}
