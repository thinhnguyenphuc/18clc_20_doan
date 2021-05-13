package com.example.photo_manager.ui.Album.AlbumDetail;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.Model.Video_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumUri.AlbumUri;
import com.example.photo_manager.ui.Album.AlbumViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddVideoAdapter extends MultiChoiceAdapter<AddVideoAdapter.ViewHolder> {

    private final Context mContext;
    private ArrayList<Video_Model> video_models = new ArrayList<>();

    private ScaleAnimation mSelectScaleAnimation;
    private ScaleAnimation mDeselectScaleAnimation;

    private AlbumViewModel viewModel;
    int albumId;

    public AddVideoAdapter(Context mContext, AlbumViewModel viewModel, int albumId) {
        this.mContext = mContext;
        this.viewModel = viewModel;
        this.albumId = albumId;
    }

    public void setData(ArrayList<Video_Model> video_models) {
        this.video_models = video_models;
        notifyDataSetChanged();
    }

    public void accept() {
        List<Integer> indexes = this.getSelectedItemList();
        for (Integer index: indexes) {
            viewModel.insertAlbumUri(new AlbumUri(video_models.get(index).getUri().toString(), albumId, 1));
        }
    }

    @Override
    public int getItemCount() {
        return video_models.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_select_item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        Glide.with(mContext).load(video_models.get(position).getUri()).apply(new RequestOptions().centerCrop()).into(holder.imageView);
    }


    @Override
    public void setActive(@NonNull View view, boolean state) {
        Log.d("ADD ITEM ADAPTER", "setActive: ");

        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
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

    @Override
    protected View.OnClickListener defaultItemViewClickListener(ViewHolder holder, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}