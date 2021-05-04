package com.example.photo_manager.ui.SlideShow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.photo_manager.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SlideShowAdapter extends
        SliderViewAdapter<SlideShowAdapter.SliderAdapterVH> {

    private Context context;
    private List<String> uris = new ArrayList<>();
    private Toolbar toolbar;

    public SlideShowAdapter(Context context, Toolbar toolbar) {
        this.context = context;
        this.toolbar = toolbar;
    }

    public void renewItems(List<String> uris) {
        this.uris = uris;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.uris.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(String sliderItem) {
        this.uris.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate =  LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_slideshow_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        viewHolder.imageView.setImage(ImageSource.uri(uris.get(position)));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return uris.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        SubsamplingScaleImageView imageView;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.imageView);
            this.itemView = itemView;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (toolbar.getVisibility() == View.INVISIBLE) {
                        toolbar.setVisibility(View.VISIBLE);
                    } else {
                        toolbar.setVisibility(View.INVISIBLE);
                    }

                }
            });
        }
    }

}