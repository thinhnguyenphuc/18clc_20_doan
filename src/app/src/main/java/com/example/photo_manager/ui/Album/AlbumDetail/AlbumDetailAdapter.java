package com.example.photo_manager.ui.Album.AlbumDetail;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.photo_manager.R;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumWithUris;
import com.example.photo_manager.ui.Album.AlbumFragmentDirections;
import com.example.photo_manager.ui.Favourite.FavouriteDababase.FavouriteItem;
import com.example.photo_manager.ui.Favourite.FavouriteFragmentDirections;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetailAdapter extends RecyclerView.Adapter<AlbumDetailAdapter.ViewHolder> {

    private Context context = null;
    private AlbumWithUris data;
    private NavController navController;
    int albumId;

    public AlbumDetailAdapter(Context context, NavController navController, int albumId) {
        this.context = context;
        this.navController = navController;
        this.albumId = albumId;
    }

    public void setData(AlbumWithUris data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.picture_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String picture_uri = data.albumUris.get(position).getUri();
        RequestOptions options = new RequestOptions();
        Glide.with(this.context).load(Uri.parse(picture_uri)).apply(options.centerCrop()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        } else {
            return data.albumUris.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.pictureView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlbumDetailFragmentDirections.ActionAlbumDetailFragmentToViewAlbumPhotoFragment action =
                            AlbumDetailFragmentDirections.actionAlbumDetailFragmentToViewAlbumPhotoFragment(
                                    data.albumUris.get(getAbsoluteAdapterPosition()).getUri(), albumId
                            );
                    navController.navigate(action);
                }
            });
        }
    }

}