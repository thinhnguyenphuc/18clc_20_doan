package com.example.photo_manager.Adapter_Picture;

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
import com.example.photo_manager.R;
import com.example.photo_manager.RecyclerViewClickInterface;
import com.example.photo_manager.Type;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;

public class View_By_Date_Picture_Adaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList data;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    public View_By_Date_Picture_Adaper(Context mContext, ArrayList data, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.data = data;
        this.context = mContext;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == Type.DATE){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View dateView = inflater.inflate(R.layout.date_item, parent, false);
            return new ViewHolderDate(dateView);
        } else{
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View pictureView = inflater.inflate(R.layout.picture_item, parent, false);
            return new ViewHolderPicture(pictureView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==Type.DATE) {
            ViewHolderDate viewHolderDate = (ViewHolderDate) holder;
            Date_Model date_model = (Date_Model) data.get(position);
            viewHolderDate.time.setText(date_model.getTime());
            FlexboxLayoutManager.LayoutParams layoutParams = (FlexboxLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFlexGrow(1.0f);
            layoutParams.width = layoutParams.MATCH_PARENT;
            holder.itemView.setLayoutParams(layoutParams);
        } else {
            ViewHolderPicture viewHolderPicture = (ViewHolderPicture) holder;
            Picture_Model picture_model = (Picture_Model) data.get(position);
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
        boolean tmp =data.get(position).getClass().equals(Date_Model.class);
        if(tmp){return Type.DATE;}
        else {return Type.IMAGE;}
    }

    @Override
    public int getItemCount() {
        return data.size();
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
    class ViewHolderDate extends RecyclerView.ViewHolder{
        TextView time;
        public ViewHolderDate(@NonNull View itemView){
            super(itemView);
            time = itemView.findViewById(R.id.textViewDate);
        }
    }
}
