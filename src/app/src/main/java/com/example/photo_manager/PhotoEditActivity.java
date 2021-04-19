package com.example.photo_manager;

import androidx.annotation.RequiresApi;
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

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.ViewType;

public class PhotoEditActivity extends AppCompatActivity {

    AlertDialog text_dialog;
    EditText text_input;
    PhotoEditor mPhotoEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        ImageButton text_button = findViewById(R.id.text_button);

        text_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoEditor.addText("Edit text here", R.color.dark);
            }


        });
        mPhotoEditor.setOnPhotoEditorListener(new OnPhotoEditorListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
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