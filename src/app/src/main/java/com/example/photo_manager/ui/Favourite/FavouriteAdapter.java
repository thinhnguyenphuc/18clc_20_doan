package com.example.photo_manager.ui.Favourite;

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
import com.example.photo_manager.ui.Favourite.FavouriteDababase.FavouriteItem;

import java.util.ArrayList;
import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

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
        return super.getItemViewType(position);
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
        String picture_uri = items.get(position).getUri();
        RequestOptions options = new RequestOptions();
        Glide.with(this.context).load(Uri.parse(picture_uri)).apply(options.centerCrop()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
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

}
