package com.example.photo_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.divyanshu.colorseekbar.ColorSeekBar;
import com.example.photo_manager.PEAdapters.PEEmojiAdapter;
import com.example.photo_manager.PEAdapters.PEFilterAdapter;
import com.example.photo_manager.PEAdapters.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.ViewType;

import static ja.burhanrashid52.photoeditor.PhotoEditor.getEmojis;

public class PhotoEditActivity extends AppCompatActivity {

    PhotoEditor mPhotoEditor;

    ImageButton undo_button, redo_button, share_button, save_button;

    ImageButton brush_button, eraser_button, text_button, filter_button, brightness_button, emoji_button;

    //brush properties
    View brush_view;
    BottomSheetDialog brush_dialog;

    //eraser properties
    View eraser_view;
    BottomSheetDialog eraser_dialog;

    //add text properties
    View add_text_view;
    AlertDialog add_text_dialog;
    View edit_text_view;
    AlertDialog edit_text_dialog;

    //filter properties
    View filter_view;
    BottomSheetDialog filter_dialog;

    //emoji properties
    View emoji_view;
    BottomSheetDialog emoji_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_edit);

        Intent intent = getIntent();
        Bitmap photoBitmap = intent.getParcelableExtra("photo");
        String photoPath =  intent.getStringExtra("path");

        PhotoEditorView mPhotoEditorView = findViewById(R.id.photoEditorView);

        mPhotoEditorView.getSource().setImageBitmap(photoBitmap);

        ImageView mPhotoEditorImageView = mPhotoEditorView.getSource();

        //Use custom font using latest support library
        Typeface mTextRobotoTf = ResourcesCompat.getFont(this, R.font.roboto_medium);

        //loading font from assest
        Typeface mEmojiTypeFace = ResourcesCompat.getFont(this, R.font.emojione_android);

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true)
                .setDefaultTextTypeface(mTextRobotoTf)
                .setDefaultEmojiTypeface(mEmojiTypeFace)
                .build();

        for (PhotoFilter pf: PhotoFilter.values()) {
            mPhotoEditor.setFilterEffect(pf);
        }

        this.undo_button = findViewById(R.id.undo_btn);
        this.redo_button = findViewById(R.id.redo_btn);
        this.share_button = findViewById(R.id.share_btn);
        this.save_button = findViewById(R.id.save_button);

        this.brush_button = findViewById(R.id.brush_button);
        this.eraser_button = findViewById(R.id.eraser_button);
        this.text_button = findViewById(R.id.text_button);
        this.filter_button = findViewById(R.id.filter_button);
        this.brightness_button = findViewById(R.id.brightness_button);
        this.emoji_button = findViewById(R.id.add_emoji_button);


        this.undo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoEditor.undo();
            }
        });

        this.redo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoEditor.redo();
            }
        });

        //brush set up _______________________________________________________________________***

        brush_view = getLayoutInflater().inflate(R.layout.pe_brush_chooser, null);
        brush_dialog = new BottomSheetDialog(PhotoEditActivity.this);
        brush_dialog.setContentView(brush_view);

        final TextView brush_current_color = brush_view.findViewById(R.id.current_color);
        final Slider brush_size_picker = brush_view.findViewById(R.id.brush_size);
        final Slider brush_opacity_picker = brush_view.findViewById(R.id.brush_opacity);
        final ColorSeekBar brush_color_picker = brush_view.findViewById(R.id.color_seek_bar);

        brush_size_picker.setValue(brush_size_picker.getValue());
        brush_opacity_picker.setValue((float) brush_opacity_picker.getValue());

        brush_current_color.setBackgroundColor(brush_color_picker.getColor());

        brush_color_picker.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int i) {
                brush_current_color.setBackgroundColor(i);
            }

        });


        brush_view.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brush_dialog.cancel();
            }
        });

        brush_view.findViewById(R.id.apply_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoEditor.setBrushColor(brush_color_picker.getColor());
                mPhotoEditor.setBrushSize(brush_size_picker.getValue());
                mPhotoEditor.setOpacity((int) brush_opacity_picker.getValue());
                brush_dialog.cancel();
            }
        });

        this.brush_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brush_dialog.show();
            }
        });


        //eraser set up _______________________________________________________________________***

        this.eraser_view = getLayoutInflater().inflate(R.layout.pe_eraser_chooser, null);
        this.eraser_dialog = new BottomSheetDialog(PhotoEditActivity.this);
        this.eraser_dialog.setContentView(this.eraser_view);

        final Slider eraser_size_picker = this.eraser_view.findViewById(R.id.eraser_size);
        eraser_size_picker.setValue(eraser_size_picker.getValue());


        this.eraser_view.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eraser_dialog.cancel();
            }
        });

        this.eraser_view.findViewById(R.id.apply_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoEditor.setBrushEraserSize(eraser_size_picker.getValue());
                mPhotoEditor.brushEraser();

                eraser_dialog.cancel();
            }
        });

        this.eraser_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eraser_dialog.show();
            }
        });


        //add and edit text set up _______________________________________________________________________***

        add_text_view = getLayoutInflater().inflate(R.layout.pe_text_editor, null);
        add_text_dialog = new AlertDialog.Builder(PhotoEditActivity.this).create();

        final int[] add_text_color = new int[1];

        final EditText add_text_input = add_text_view.findViewById(R.id.text);
        final TextView current_color = add_text_view.findViewById(R.id.current_color);
        final ColorSeekBar add_text_color_picker = add_text_view.findViewById(R.id.color_seek_bar);

        add_text_color[0] = getColor(R.color.init_color);
        current_color.setBackgroundColor(add_text_color[0]);

        add_text_color_picker.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int i) {
                add_text_color[0] = i;
                current_color.setBackgroundColor(i);
            }

        });

        add_text_dialog.setView(add_text_view);
        add_text_dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPhotoEditor.addText( add_text_input.getText().toString(), add_text_color[0]);
            }
        });
        add_text_dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                add_text_dialog.cancel();
            }
        });

        text_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_text_input.setText(getString(R.string.text_holder));
                add_text_dialog.show();
            }


        });

        edit_text_view = getLayoutInflater().inflate(R.layout.pe_text_editor, null);
        edit_text_dialog = new AlertDialog.Builder(PhotoEditActivity.this).create();

        mPhotoEditor.setOnPhotoEditorListener(new OnPhotoEditorListener() {
            @Override
            public void onEditTextChangeListener(final View rootView, String text, final int colorCode) {

                final int[] color = new int[1];

                final EditText text_input = edit_text_view.findViewById(R.id.text);
                final TextView current_color = edit_text_view.findViewById(R.id.current_color);
                final ColorSeekBar colorSeekBar = edit_text_view.findViewById(R.id.color_seek_bar);

                text_input.setText(text);

                color[0] = colorCode;
                current_color.setBackgroundColor(colorCode);

                colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                    @Override
                    public void onColorChangeListener(int i) {
                        color[0] = i;
                        current_color.setBackgroundColor(i);
                    }

                });

                edit_text_dialog.setView(edit_text_view);
                edit_text_dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPhotoEditor.editText(rootView, text_input.getText().toString(), color[0]);
                    }
                });
                edit_text_dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edit_text_dialog.cancel();
                    }
                });

                edit_text_dialog.show();

            }

            @Override
            public void onAddViewListener(ViewType viewType, int i) {

            }

            @Override
            public void onRemoveViewListener(ViewType viewType, int i) {

            }

            @Override
            public void onStartViewChangeListener(ViewType viewType) {

            }

            @Override
            public void onStopViewChangeListener(ViewType viewType) {

            }
        });

        //filter set up _______________________________________________________________________***

        filter_view = getLayoutInflater().inflate(R.layout.pe_filter_chooser, null);
        filter_dialog = new BottomSheetDialog(PhotoEditActivity.this);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(PhotoEditActivity.this, LinearLayoutManager.HORIZONTAL, false);

        RecyclerView myList = (RecyclerView) filter_view.findViewById(R.id.filter_list);
        myList.setLayoutManager(layoutManager);
        myList.setAdapter(new PEFilterAdapter(PhotoEditActivity.this, mPhotoEditor, PhotoFilter.values()));

        filter_dialog.setContentView(filter_view);

        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_dialog.show();
            }
        });


        //emoji set up _______________________________________________________________________***

        emoji_view = getLayoutInflater().inflate(R.layout.pe_emoji_chooser, null);
        emoji_dialog = new BottomSheetDialog(this);

        emoji_dialog.setContentView(emoji_view);

        int mNoOfColumns = Utility.calculateNoOfColumns(getApplicationContext(), 10f + Float.parseFloat(getString(R.string.total_emoji_width)));

        GridLayoutManager emojiLayoutManager = new GridLayoutManager(this, mNoOfColumns);
        RecyclerView emojiList = emoji_view.findViewById(R.id.emoji_list);
        emojiList.setLayoutManager(emojiLayoutManager);

        ArrayList<String> emoji_unicodes = PhotoEditor.getEmojis(PhotoEditActivity.this);

        emojiList.setAdapter(new PEEmojiAdapter(emojiList, mPhotoEditor, emoji_unicodes, emoji_dialog));

        emoji_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emoji_dialog.show();

            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}