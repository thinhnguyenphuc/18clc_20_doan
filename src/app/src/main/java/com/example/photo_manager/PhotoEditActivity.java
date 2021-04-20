package com.example.photo_manager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.divyanshu.colorseekbar.ColorSeekBar;
import com.example.photo_manager.PEAdapters.PEFilterAdapter;
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
    float brush_size;
    int brush_opacity;
    int brush_color;

    //eraser properties
    float eraser_size;

    //filter properties

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_edit);

        PhotoEditorView mPhotoEditorView = findViewById(R.id.photoEditorView);

        mPhotoEditorView.getSource().setImageResource(R.drawable.photo);

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

        this.brush_size = Float.parseFloat(getString(R.string.brush_size_max));
        this.brush_opacity = Integer.parseInt(getString(R.string.brush_opacity_max));
        this.brush_color = getColor(R.color.init_color);

        this.eraser_size = Float.parseFloat(getString(R.string.brush_size_max));

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


        this.brush_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = getLayoutInflater().inflate(R.layout.pe_brush_chooser, null);
                final BottomSheetDialog dialog = new BottomSheetDialog(PhotoEditActivity.this);
                dialog.setContentView(view);

                dialog.show();

                final TextView textView = view.findViewById(R.id.current_color);
                final Slider brush_size_picker = view.findViewById(R.id.brush_size);
                final Slider brush_opacity_picker = view.findViewById(R.id.brush_opacity);
                final ColorSeekBar colorSeekBar = view.findViewById(R.id.color_seek_bar);

                brush_size_picker.setValue(brush_size);
                brush_opacity_picker.setValue((float) brush_opacity);

                textView.setBackgroundColor(brush_color);

                colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                    @Override
                    public void onColorChangeListener(int i) {
                        textView.setBackgroundColor(i);
                    }

                });

                ImageButton cancel_button = view.findViewById(R.id.cancel_button);
                ImageButton apply_button = view.findViewById(R.id.apply_button);

                cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                apply_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        brush_size = brush_size_picker.getValue();
                        brush_opacity = (int) brush_opacity_picker.getValue();
                        brush_color = colorSeekBar.getColor();

                        mPhotoEditor.setBrushColor(brush_color);
                        mPhotoEditor.setBrushSize(brush_size);
                        mPhotoEditor.setOpacity(brush_opacity);
                        dialog.cancel();
                    }
                });
            }
        });

        this.eraser_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = getLayoutInflater().inflate(R.layout.pe_eraser_chooser, null);
                final BottomSheetDialog dialog = new BottomSheetDialog(PhotoEditActivity.this);
                dialog.setContentView(view);

                dialog.show();

                final Slider eraser_size_picker = view.findViewById(R.id.eraser_size);
                eraser_size_picker.setValue(eraser_size);

                ImageButton cancel_button = view.findViewById(R.id.cancel_button);
                ImageButton apply_button = view.findViewById(R.id.apply_button);

                cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                apply_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eraser_size = eraser_size_picker.getValue();

                        mPhotoEditor.setBrushEraserSize(eraser_size);
                        mPhotoEditor.brushEraser();

                        dialog.cancel();
                    }
                });
            }
        });

        text_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.pe_text_editor, null);
                final AlertDialog text_dialog = new AlertDialog.Builder(PhotoEditActivity.this).create();

                final int[] color = new int[1];

                final EditText text_input = view.findViewById(R.id.text);
                final TextView current_color = view.findViewById(R.id.current_color);
                final ColorSeekBar colorSeekBar = view.findViewById(R.id.color_seek_bar);

                color[0] = getColor(R.color.init_color);
                current_color.setBackgroundColor(color[0]);

                colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                    @Override
                    public void onColorChangeListener(int i) {
                        color[0] = i;
                        current_color.setBackgroundColor(i);
                    }

                });

                text_dialog.setView(view);
                text_dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPhotoEditor.addText( text_input.getText().toString(), color[0]);
                    }
                });
                text_dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        text_dialog.cancel();
                    }
                });

                text_dialog.show();
            }


        });

        mPhotoEditor.setOnPhotoEditorListener(new OnPhotoEditorListener() {
            @Override
            public void onEditTextChangeListener(final View rootView, String text, final int colorCode) {
                View view = getLayoutInflater().inflate(R.layout.pe_text_editor, null);
                final AlertDialog text_dialog = new AlertDialog.Builder(PhotoEditActivity.this).create();

                final int[] color = new int[1];

                final EditText text_input = view.findViewById(R.id.text);
                final TextView current_color = view.findViewById(R.id.current_color);
                final ColorSeekBar colorSeekBar = view.findViewById(R.id.color_seek_bar);

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

                text_dialog.setView(view);
                text_dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPhotoEditor.editText(rootView, text_input.getText().toString(), color[0]);
                    }
                });
                text_dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        text_dialog.cancel();
                    }
                });

                text_dialog.show();

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

        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog dialog = new BottomSheetDialog(PhotoEditActivity.this);

                View filter_chooser_view = getLayoutInflater().inflate(R.layout.pe_filter_chooser, null);
                LinearLayoutManager layoutManager
                        = new LinearLayoutManager(PhotoEditActivity.this, LinearLayoutManager.HORIZONTAL, false);

                RecyclerView myList = (RecyclerView) filter_chooser_view.findViewById(R.id.filter_list);
                myList.setLayoutManager(layoutManager);
                myList.setAdapter(new PEFilterAdapter(PhotoEditActivity.this, mPhotoEditor, PhotoFilter.values()));

                dialog.setContentView(filter_chooser_view);
                dialog.show();
            }
        });

        emoji_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> unicodes = PhotoEditor.getEmojis(PhotoEditActivity.this);
                mPhotoEditor.addEmoji(unicodes.get(0));
            }
        });
    }
}