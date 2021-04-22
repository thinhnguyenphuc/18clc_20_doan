package com.example.photo_manager.Adapter;

import android.content.Context;
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
    private ArrayList<Picture_Model> picture_models;

    public Picture_Adapter_All(Context mContext, ArrayList<Picture_Model> picture_models
            , RecyclerViewClickInterface recyclerViewClickInterface){
        this.context = mContext;
        this.picture_models = picture_models;
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
        Picture_Model picture_model = picture_models.get(position);
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(this.context).load(picture_model.getUri()).apply(options).into(viewHolderPicture.imageView);
    }

    @Override
    public int getItemCount() {
        return picture_models.size();
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
