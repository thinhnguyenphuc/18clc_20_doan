package com.example.photo_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Picture_Adapter extends RecyclerView.Adapter<Picture_Adapter.ViewHolder> {
    private Context context;
    private ArrayList<Picture_Model> picture_models;

    public Picture_Adapter(Context mContext, ArrayList<Picture_Model> picture_models) {
        this.picture_models = picture_models;
        this.context = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View pictureView = inflater.inflate(R.layout.picture_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(pictureView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picture_Model picture_model = picture_models.get(position);
        Glide.with(this.context).load(picture_model.getUri()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.pictureView);
            }
        }
}
