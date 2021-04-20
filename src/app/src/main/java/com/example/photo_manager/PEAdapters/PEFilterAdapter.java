package com.example.photo_manager.PEAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.photo_manager.R;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoFilter;

public class PEFilterAdapter extends RecyclerView.Adapter<PEFilterAdapter.ViewHolder> {
    private Context context;
    private PhotoFilter[] filters;
    private PhotoEditor mPhotoEditor;

    public PEFilterAdapter(Context context,PhotoEditor pe, PhotoFilter[] filters) {
        this.context = context;
        this.filters = filters;
        this.mPhotoEditor = pe;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Button button;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            button =  view.findViewById(R.id.button);
        }

        public Button getButton() {
            return this.button;
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pe_filter_chooser_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Button button = viewHolder.getButton();
        button.setText(filters[position].name());
        button.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                mPhotoEditor.setFilterEffect(filters[position]);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return filters.length;
    }
}
