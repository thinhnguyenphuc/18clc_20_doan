package com.example.photo_manager.ui.Favourite;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter;
import com.example.photo_manager.Adapter.Picture_Adapter_All;
import com.example.photo_manager.Adapter.View_By_Date_Picture_Adaper;
import com.example.photo_manager.Model.Date_Model;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.Model.Video_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.RecyclerViewClickInterface;
import com.example.photo_manager.Type;
import com.example.photo_manager.ui.Favourite.FavouriteDababase.FavouriteItem;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class FavouriteAdapter extends MultiChoiceAdapter<RecyclerView.ViewHolder> {

    private  Context context = null;
    private List<FavouriteItem> items = new ArrayList<>();
    private NavController navController;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    public FavouriteAdapter(Context context, NavController navController,
                            RecyclerViewClickInterface recyclerViewClickInterface) {
        this.context = context;
        this.navController = navController;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View pictureView;
        if(viewType == Type.VIDEO){
            pictureView = inflater.inflate(R.layout.multi_select_item_video, parent, false);
            return new ViewHolderVideo(pictureView);
        } else {
            pictureView = inflater.inflate(R.layout.multi_select_item, parent, false);
            return new ViewHolderImage(pictureView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder,position);
        if (getItemViewType(position)==Type.VIDEO){
            ViewHolderVideo viewHolderVideo = (ViewHolderVideo) holder;
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            Glide.with(this.context).load(items.get(position).getUri()).apply(options).into(viewHolderVideo.videoView);
            FlexboxLayoutManager.LayoutParams layoutParams = (FlexboxLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.width = getScreenWidth()/4;
            holder.itemView.setLayoutParams(layoutParams);
        } else {
            ViewHolderImage viewHolderPicture = (ViewHolderImage) holder;
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            Glide.with(this.context).load(items.get(position).getUri()).apply(options).into(viewHolderPicture.imageView);
            FlexboxLayoutManager.LayoutParams layoutParams = (FlexboxLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.width = getScreenWidth()/4;
            holder.itemView.setLayoutParams(layoutParams);
        }
    }

    @Override
    protected View.OnClickListener defaultItemViewClickListener(RecyclerView.ViewHolder holder, int position) {
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) { recyclerViewClickInterface.onItemClick(position);}
        };
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolderImage extends RecyclerView.ViewHolder {

        ImageView imageView;

        ViewHolderImage(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FavouriteFragmentDirections.ActionFavouriteFragmentToViewPhotoFragment action =
                            FavouriteFragmentDirections.actionFavouriteFragmentToViewPhotoFragment(items.get(getAdapterPosition()).getUri());
                    navController.navigate(action);
                }
            });

        }
    }
    class ViewHolderVideo extends RecyclerView.ViewHolder {

        ImageView videoView;

        ViewHolderVideo(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.multi_video_view);
            videoView.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void setActive(@NonNull View view, boolean state) {
        Log.d("ADD ITEM ADAPTER", "setActive: " );
        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
        ImageView videoView = (ImageView) view.findViewById(R.id.multi_video_view);
        final ImageView tickImage = (ImageView) view.findViewById(R.id.tick_image);

        if(imageView==null){
            if (state) {
                videoView.setScaleX(0.7f);
                videoView.setScaleY(0.7f);
                tickImage.setVisibility(View.VISIBLE);
            } else {
                videoView.setScaleX(1f);
                videoView.setScaleY(1f);
                tickImage.setVisibility(View.INVISIBLE);
            }
        } else {
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

}
