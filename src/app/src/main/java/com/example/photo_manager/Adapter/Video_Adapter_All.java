package com.example.photo_manager.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter;
import com.example.photo_manager.Model.Video_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.RecyclerViewClickInterface;

import java.util.ArrayList;
import java.util.HashMap;

public class Video_Adapter_All extends MultiChoiceAdapter<Video_Adapter_All.ViewHolder> {
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
    public Video_Adapter_All.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View pictureView = inflater.inflate(R.layout.multi_select_item_video, parent, false);
        return new Video_Adapter_All.ViewHolder(pictureView);
    }

    @Override
    public int getItemCount() {
        return video_models.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Uri picture_uri = video_models.get(position).getUri();
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(this.context).load(picture_uri).apply(options).into(holder.imageView);
    }

    @Override
    protected View.OnClickListener defaultItemViewClickListener(Video_Adapter_All.ViewHolder holder, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClickInterface.onItemClick(position);
            }
        };
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.multi_video_view);
        }
    }
    @Override
    public void setActive(@NonNull View view, boolean state) {
        Log.d("ADD ITEM ADAPTER", "setActive: " );
        ImageView imageView = (ImageView) view.findViewById(R.id.multi_video_view);
        final ImageView tickImage = (ImageView) view.findViewById(R.id.tick_image);

        if (state) {
            imageView.setScaleX(0.7f);
            imageView.setScaleY(0.7f);
            tickImage.setVisibility(View.VISIBLE);
        } else {
            imageView.setScaleX(1f);
            imageView.setScaleY(1f);
            tickImage.setVisibility(View.INVISIBLE);
        }
    }
}
