package com.example.photo_manager.Adapter_Video;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.request.RequestOptions;

import com.crust87.texturevideoview.widget.TextureVideoView;
import com.example.photo_manager.Model.Video_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.RecyclerViewClickInterface;

import java.util.ArrayList;

public class Video_Adapter_All extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private RecyclerViewClickInterface recyclerViewClickInterface;
    private Context context;
    private ArrayList<Video_Model> video_models = new ArrayList<>();

    public Video_Adapter_All(Context mContext
            , RecyclerViewClickInterface recyclerViewClickInterface){
        this.context = mContext;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    public void setVideos(ArrayList<Video_Model> video_models) {
        this.video_models = video_models;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View videoView = inflater.inflate(R.layout.video_item, parent, false);
        return new ViewHolderVideo(videoView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Video_Adapter_All.ViewHolderVideo viewHolderVideo = (Video_Adapter_All.ViewHolderVideo) holder;
        Uri video_uri = video_models.get(position).getUri();
        viewHolderVideo.videoView.setVideoURI(video_uri);
        viewHolderVideo.videoView.start();
    }

    @Override
    public int getItemCount() {
        return video_models.size();
    }

    class ViewHolderVideo extends RecyclerView.ViewHolder {
        TextureVideoView videoView;
        public ViewHolderVideo(@NonNull View itemView) {
            super(itemView);
            videoView = (TextureVideoView) itemView.findViewById(R.id.videoView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
