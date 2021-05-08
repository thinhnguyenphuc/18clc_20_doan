package com.example.photo_manager.Adapter_Video;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.crust87.texturevideoview.widget.TextureVideoView;
import com.example.photo_manager.MainActivity;
import com.example.photo_manager.Model.Video_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.RecyclerViewClickInterface;

import java.util.ArrayList;
import java.util.HashMap;

public class Video_Adapter_All extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private RecyclerViewClickInterface recyclerViewClickInterface;
    private Context context;
    private ArrayList<Video_Model> video_models = new ArrayList<>();

    public Video_Adapter_All(Context mContext
            , RecyclerViewClickInterface recyclerViewClickInterface){
        this.context = mContext;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    public void setVideos(ArrayList<Video_Model> video_models) {
        this.video_models = video_models;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View videoView = inflater.inflate(R.layout.video_item, parent, false);
        return new ViewHolderVideo(videoView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Video_Adapter_All.ViewHolderVideo viewHolderVideo = (Video_Adapter_All.ViewHolderVideo) holder;
        Uri video_uri = video_models.get(position).getUri();
        RequestOptions cropOptions = new RequestOptions().centerCrop();
        Glide.with(context)
                .load(video_uri)
                .apply(cropOptions)
                .into(viewHolderVideo.videoView);
    }

    @Override
    public int getItemCount() {
        return video_models.size();
    }

    class ViewHolderVideo extends RecyclerView.ViewHolder {
        ImageView videoView;
        public ViewHolderVideo(@NonNull View itemView) {
            super(itemView);
            videoView = (ImageView) itemView.findViewById(R.id.videoView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}
