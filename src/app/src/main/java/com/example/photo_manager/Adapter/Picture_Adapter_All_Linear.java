package com.example.photo_manager.Adapter;

import android.content.Context;
import android.graphics.Picture;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.RecyclerViewClickInterface;
import com.example.photo_manager.Utility;

import java.util.ArrayList;

public class Picture_Adapter_All_Linear extends MultiChoiceAdapter<Picture_Adapter_All_Linear.ViewHolder> {
    private RecyclerViewClickInterface recyclerViewClickInterface;
    private Context context;
    private ArrayList<Picture_Model> picture_models = new ArrayList<>();
    private ScaleAnimation mSelectScaleAnimation;
    private ScaleAnimation mDeselectScaleAnimation;

    public Picture_Adapter_All_Linear(Context mContext
            , RecyclerViewClickInterface recyclerViewClickInterface){
        this.context = mContext;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    public void setPictures(ArrayList<Picture_Model> picture_models) {
        this.picture_models = picture_models;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View pictureView = inflater.inflate(R.layout.picture_item_linear, parent, false);
        return new ViewHolder(pictureView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        Picture_Model picture_model = picture_models.get(position);
        Uri picture_uri = picture_model.getUri();
        String name = picture_model.getName();
        String date = picture_model.getTime();
        String storage = Utility.getRealPathFromUri(context, picture_uri);

        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(this.context).load(picture_uri).apply(options).into(viewHolder.imageView);

        viewHolder.name.setText(name);
        viewHolder.date.setText(date);
        viewHolder.storage.setText(storage);
    }



    @Override
    public int getItemCount() {
        return picture_models.size();
    }


    @Override
    public void setActive(@NonNull View view, boolean state) {
        Log.d("ADD ITEM ADAPTER", "setActive: " );
        ImageView imageView = (ImageView) view.findViewById(R.id.pictureView);
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
                recyclerViewClickInterface.onItemClick(position);
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, date, storage;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.pictureView);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            storage = itemView.findViewById(R.id.storage);
        }
    }
}
