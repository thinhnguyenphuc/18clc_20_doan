package com.example.photo_manager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class PhotoEditActivity extends AppCompatActivity {

    AlertDialog text_dialog;
    EditText text_input;
    PhotoEditor mPhotoEditor;


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


    }
}