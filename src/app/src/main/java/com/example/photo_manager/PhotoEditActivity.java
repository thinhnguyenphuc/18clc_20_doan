package com.example.photo_manager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.divyanshu.colorseekbar.ColorSeekBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.Slider;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.ViewType;

public class PhotoEditActivity extends AppCompatActivity {

    AlertDialog text_dialog;
    EditText text_input;
    PhotoEditor mPhotoEditor;

    ImageButton brush_button, eraser_button, text_button, filter_button, brightness_button;

    //brush properties
    float brush_size;
    int brush_opacity;

    //eraser properties
    float eraser_size;


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

        this.brush_button = findViewById(R.id.brush_button);
        this.eraser_button = findViewById(R.id.eraser_button);
        this.text_button = findViewById(R.id.text_button);
        this.filter_button = findViewById(R.id.filter_button);
        this.brightness_button = findViewById(R.id.brightness_button);

        this.brush_size = Float.parseFloat(getString(R.string.brush_size_max));
        this.brush_opacity = Integer.parseInt(getString(R.string.brush_opacity_max));

        this.eraser_size = Float.parseFloat(getString(R.string.brush_size_max));


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

                textView.setBackgroundColor(colorSeekBar.getColor());

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

                        mPhotoEditor.setBrushColor(colorSeekBar.getColor());
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
                mPhotoEditor.addText("Edit text here", R.color.pe_bg);
            }


        });

        mPhotoEditor.setOnPhotoEditorListener(new OnPhotoEditorListener() {
            @Override
            public void onEditTextChangeListener(final View rootView, String text, final int colorCode) {
                TextView textView = rootView.findViewById(R.id.tvPhotoEditorText);
                text_dialog = new AlertDialog.Builder(PhotoEditActivity.this).create();
                text_input = new EditText(PhotoEditActivity.this);

                text_input.setText(textView.getText());

                text_dialog.setView(text_input);
                text_dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPhotoEditor.editText(rootView, text_input.getText().toString(), colorCode);
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
    }
}