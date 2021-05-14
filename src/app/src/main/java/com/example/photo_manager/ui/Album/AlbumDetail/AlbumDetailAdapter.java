package com.example.photo_manager.ui.Album.AlbumDetail;

import android.content.Context;
import android.content.res.Resources;
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
import com.example.photo_manager.Type;
import com.example.photo_manager.ui.Album.AlbumDatabase.AlbumWithUris;
import com.example.photo_manager.ui.Favourite.FavouriteAdapter;
import com.example.photo_manager.ui.Favourite.FavouriteFragmentDirections;
import com.google.android.flexbox.FlexboxLayoutManager;

public class AlbumDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    public int getItemViewType(int position) {
        if(data.albumUris.get(position).getType()==1){
            return Type.VIDEO;
        } else return Type.IMAGE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == Type.VIDEO){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View videoView = inflater.inflate(R.layout.video_item, parent, false);
            return new AlbumDetailAdapter.ViewHolderVideo(videoView);
        } else{
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View pictureView = inflater.inflate(R.layout.picture_item, parent, false);
            return new AlbumDetailAdapter.ViewHolderPicture(pictureView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)== Type.VIDEO) {
            AlbumDetailAdapter.ViewHolderVideo viewHolderVideo = (AlbumDetailAdapter.ViewHolderVideo) holder;
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            Glide.with(this.context).load(data.albumUris.get(position).getUri()).apply(options).into(viewHolderVideo.videoView);
            FlexboxLayoutManager.LayoutParams layoutParams = (FlexboxLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.width = getScreenWidth()/4;
            holder.itemView.setLayoutParams(layoutParams);
        } else {
            AlbumDetailAdapter.ViewHolderPicture viewHolderPicture = (AlbumDetailAdapter.ViewHolderPicture) holder;
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            Glide.with(this.context).load(data.albumUris.get(position).getUri()).apply(options).into(viewHolderPicture.imageView);
            FlexboxLayoutManager.LayoutParams layoutParams = (FlexboxLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.width = getScreenWidth()/4;
            holder.itemView.setLayoutParams(layoutParams);
        }
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        } else {
            return data.albumUris.size();
        }
    }
    public class ViewHolderPicture extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolderPicture(@NonNull View itemView) {
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
    public class ViewHolderVideo extends RecyclerView.ViewHolder {
        ImageView videoView;
        public ViewHolderVideo(@NonNull View itemView) {
            super(itemView);
            videoView = (ImageView) itemView.findViewById(R.id.videoView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlbumDetailFragmentDirections.ActionAlbumDetailFragmentToViewVideo action =
                            AlbumDetailFragmentDirections.actionAlbumDetailFragmentToViewVideo(
                                    data.albumUris.get(getAbsoluteAdapterPosition()).getUri()
                            );
                    navController.navigate(action);
                }
            });
        }
    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

}