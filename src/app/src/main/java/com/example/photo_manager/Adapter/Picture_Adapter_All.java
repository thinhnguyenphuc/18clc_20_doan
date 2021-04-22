package com.example.photo_manager.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.photo_manager.Picture_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.RecyclerViewClickInterface;

import java.util.ArrayList;

public class Picture_Adapter_All extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private RecyclerViewClickInterface recyclerViewClickInterface;
    private Context context;
    private ArrayList<Uri> pictures_uri;

    public Picture_Adapter_All(Context mContext, ArrayList<Uri> pictures_uri
            , RecyclerViewClickInterface recyclerViewClickInterface){
        this.context = mContext;
        this.pictures_uri = pictures_uri;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View pictureView = inflater.inflate(R.layout.picture_item, parent, false);
        return new ViewHolderPicture(pictureView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Picture_Adapter_All.ViewHolderPicture viewHolderPicture = (Picture_Adapter_All.ViewHolderPicture) holder;
        Uri picture_uri = pictures_uri.get(position);
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(this.context).load(picture_uri).apply(options).into(viewHolderPicture.imageView);
    }

    @Override
    public int getItemCount() {
        return pictures_uri.size();
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
}
