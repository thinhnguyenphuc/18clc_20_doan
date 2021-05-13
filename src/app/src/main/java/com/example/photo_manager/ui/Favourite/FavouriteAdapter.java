package com.example.photo_manager.ui.Favourite;

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
import com.example.photo_manager.Adapter.View_By_Date_Picture_Adaper;
import com.example.photo_manager.Model.Date_Model;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.Model.Video_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.Type;
import com.example.photo_manager.ui.Favourite.FavouriteDababase.FavouriteItem;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  Context context = null;
    private List<FavouriteItem> items = new ArrayList<>();
    private NavController navController;

    public FavouriteAdapter(Context context, NavController navController) {
        this.context = context;
        this.navController = navController;
    }

    public void setItems(List<FavouriteItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(items.get(position).getType()==1){
            return Type.VIDEO;
        } else return Type.IMAGE;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == Type.VIDEO){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View videoView = inflater.inflate(R.layout.video_item, parent, false);
            return new FavouriteAdapter.ViewHolderVideo(videoView);
        } else{
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View pictureView = inflater.inflate(R.layout.picture_item, parent, false);
            return new FavouriteAdapter.ViewHolderPicture(pictureView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==Type.VIDEO) {
            ViewHolderVideo viewHolderVideo = (ViewHolderVideo) holder;
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            Glide.with(this.context).load(items.get(position).getUri()).apply(options).into(viewHolderVideo.videoView);
            FlexboxLayoutManager.LayoutParams layoutParams = (FlexboxLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.width = getScreenWidth()/4;
            holder.itemView.setLayoutParams(layoutParams);
        } else {
            ViewHolderPicture viewHolderPicture = (ViewHolderPicture) holder;
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            Glide.with(this.context).load(items.get(position).getUri()).apply(options).into(viewHolderPicture.imageView);
            FlexboxLayoutManager.LayoutParams layoutParams = (FlexboxLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.width = getScreenWidth()/4;
            holder.itemView.setLayoutParams(layoutParams);
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolderPicture extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolderPicture(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.pictureView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FavouriteFragmentDirections.ActionFavouriteFragmentToViewPhotoFragment action =
                            FavouriteFragmentDirections.actionFavouriteFragmentToViewPhotoFragment(items.get(getAdapterPosition()).getUri().toString());
                    navController.navigate(action);
                }
            });
        }
    }
    class ViewHolderVideo extends RecyclerView.ViewHolder {
        ImageView videoView;
        public ViewHolderVideo(@NonNull View itemView) {
            super(itemView);
            videoView = (ImageView) itemView.findViewById(R.id.videoView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FavouriteFragmentDirections.ActionFavouriteFragmentToViewVideoFragment action =
                            FavouriteFragmentDirections.actionFavouriteFragmentToViewVideoFragment(items.get(getAdapterPosition()).getUri());
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
