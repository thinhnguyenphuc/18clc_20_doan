package com.example.photo_manager.ui.Album;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.photo_manager.R;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumWithUris;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    List<AlbumWithUris> data = new ArrayList<>();
    Context context;
    NavController navController;

    public AlbumAdapter(Context context, NavController navController) {
        this.context = context;
        this.navController = navController;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_album_item, null);
        return new ViewHolder(view);
    }

    public void setData(List<AlbumWithUris> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AlbumWithUris albumWithUris = data.get(position);
        int size = albumWithUris.albumUris.size();
        if (size > 0) {
            Glide.with(context)
                    .load(albumWithUris.albumUris.get(0).getUri())
                    .transform(new CenterCrop(),new RoundedCorners(25))
                    .into(holder.thumbnail);
        } else {
            Glide.with(context)
                    .load(R.drawable.album_view_holder)
                    .transform(new CenterCrop(),new RoundedCorners(25))
                    .into(holder.thumbnail);
        }
        holder.name.setText(albumWithUris.album.getTitle());
        holder.size.setText(String.valueOf(size));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnail;
        private TextView name;
        private TextView size;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.album_thumbnail);
            name = itemView.findViewById(R.id.album_name);
            size = itemView.findViewById(R.id.album_size);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlbumFragmentDirections.ActionAlbumFragmentToAlbumDetailFragment action =
                            AlbumFragmentDirections
                                    .actionAlbumFragmentToAlbumDetailFragment(
                                            data.get(getAbsoluteAdapterPosition()).album.getId());
                    navController.navigate(action);
                }
            });
        }
    }
}
