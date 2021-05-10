package com.example.photo_manager.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.photo_manager.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import ja.burhanrashid52.photoeditor.PhotoEditor;

public class PEEmojiAdapter extends RecyclerView.Adapter<PEEmojiAdapter.ViewHolder> {
    private RecyclerView recyclerView;
    private ArrayList<String> unicodes;
    private PhotoEditor mPhotoEditor;
    private BottomSheetDialog dialog;

    public PEEmojiAdapter(RecyclerView recyclerView, PhotoEditor pe, ArrayList<String> unicodes, BottomSheetDialog dialog) {
        this.recyclerView = recyclerView;
        this.unicodes = unicodes;
        this.mPhotoEditor = pe;
        this.dialog = dialog;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView emoji_text_view;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            emoji_text_view =  view.findViewById(R.id.emoji);
        }

        public TextView getTextView() {
            return this.emoji_text_view;
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pe_emoji_chooser_item, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Log.d("DEBUG", "onCreate: " + unicodes.get(position));
        viewHolder.getTextView().setText(unicodes.get(position));
        viewHolder.getTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoEditor.addEmoji(unicodes.get(position));
                dialog.cancel();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return unicodes.size();
    }

}
