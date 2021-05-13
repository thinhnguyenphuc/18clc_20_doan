package com.example.photo_manager.ui.SecureFolder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter;
import com.example.photo_manager.R;
import com.example.photo_manager.ui.SecureFolder.SecureFolderViewModel.SecureFolderViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SecureFolderAdapter extends MultiChoiceAdapter<SecureFolderAdapter.ViewHolder> {

    private final Context mContext;
    private NavController navController;
    private List<File> files = new ArrayList<>();
    private SecureFolderViewModel viewModel;

    private ScaleAnimation mSelectScaleAnimation;
    private ScaleAnimation mDeselectScaleAnimation;


    SecureFolderAdapter(Context mContext, NavController navController, SecureFolderViewModel viewModel) {
        this.mContext = mContext;
        this.navController = navController;
        this.viewModel = viewModel;
    }

    public void setData(List<File> files) {
        this.files = files;
        notifyDataSetChanged();
    }

    public void accept() {
        List<Integer> indexes = this.getSelectedItemList();
        for (Integer index: indexes) {

        }
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_select_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        Glide.with(mContext)
                .load(files.get(position))
                .optionalCenterCrop()
                .into(holder.imageView);
    }


    @Override
    public void setActive(@NonNull View view, boolean state) {
        Log.d("ADD ITEM ADAPTER", "setActive: ");

        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
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
                NavDirections action = null;
                try {
                    action = SecureFolderFragmentDirections.actionSecurityFolderFragmentToViewSFPhotoFragment(files.get(position).getCanonicalPath());
                    navController.navigate(action);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }

    public void deleteSelectedItem() {
        List<Integer> indexes = this.getSelectedItemList();
        List<File> deselectItems = new ArrayList<>();

        for (Integer index: indexes) {
            deselectItems.add(files.get(index));
        }

        viewModel.deleteListOfFile(deselectItems);
    }
}