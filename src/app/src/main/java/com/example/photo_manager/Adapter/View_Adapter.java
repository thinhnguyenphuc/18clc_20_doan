package com.example.photo_manager.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.photo_manager.Model.Date_Model;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.Model.Video_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.RecyclerViewClickInterface;
import com.example.photo_manager.Type;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;

public class View_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList arrayList;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    public View_Adapter(Context context,ArrayList arrayList, RecyclerViewClickInterface recyclerViewClickInterface){
        this.context = context;
        this.arrayList = arrayList;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == Type.IMAGE){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View pictureView = inflater.inflate(R.layout.picture_item, parent, false);
            return new View_Adapter.ViewHolderPicture(pictureView);
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View pictureView = inflater.inflate(R.layout.picture_item, parent, false);
            return new View_Adapter.ViewHolderVideo(pictureView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==Type.VIDEO) {
            Picture_Adapter.ViewHolderPicture viewHolderPicture = (Picture_Adapter.ViewHolderPicture) holder;
            Picture_Model picture_model = (Picture_Model) arrayList.get(position);
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            Glide.with(this.context).load(picture_model.getUri()).apply(options).into(viewHolderPicture.imageView);
            FlexboxLayoutManager.LayoutParams layoutParams = (FlexboxLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.width = getScreenWidth()/4;
            holder.itemView.setLayoutParams(layoutParams);
        } else {
            Picture_Adapter.ViewHolderPicture viewHolderPicture = (Picture_Adapter.ViewHolderPicture) holder;
            Picture_Model picture_model = (Picture_Model) arrayList.get(position);
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            Glide.with(this.context).load(picture_model.getUri()).apply(options).into(viewHolderPicture.imageView);
            FlexboxLayoutManager.LayoutParams layoutParams = (FlexboxLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.width = getScreenWidth()/4;
            holder.itemView.setLayoutParams(layoutParams);
        }
    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public int getItemViewType(int position) {
        boolean tmp =arrayList.get(position).getClass().equals(Video_Model.class);
        if(tmp){return Type.VIDEO;}
        else {return Type.IMAGE;}
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolderPicture extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolderPicture(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.pictureView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
    class ViewHolderVideo extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolderVideo(@NonNull View itemView){
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.pictureView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
