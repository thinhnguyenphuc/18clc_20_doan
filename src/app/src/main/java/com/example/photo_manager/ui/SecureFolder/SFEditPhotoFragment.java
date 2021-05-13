package com.example.photo_manager.ui.SecureFolder;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.divyanshu.colorseekbar.ColorSeekBar;
import com.example.photo_manager.Adapter.PEEmojiAdapter;
import com.example.photo_manager.Adapter.PEFilterAdapter;
import com.example.photo_manager.R;
import com.example.photo_manager.Utility;
import com.example.photo_manager.ui.SecureFolder.SecureFolderViewModel.SecureFolderViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.Slider;

import java.io.File;
import java.util.ArrayList;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.ViewType;

public class SFEditPhotoFragment extends Fragment {

    File file;

    SecureFolderViewModel secureFolderViewModel;

    Toolbar toolbar_top;

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

    final static int REQUEST_PERMISSION_CODE = 100;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_edit_photo, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        ViewModelProvider viewModelProvider = new ViewModelProvider(requireActivity());
        secureFolderViewModel = viewModelProvider.get(SecureFolderViewModel.class);

        file = new File(SFEditPhotoFragmentArgs.fromBundle(getArguments()).getFilePath());

        PhotoEditorView mPhotoEditorView = root.findViewById(R.id.photoEditorView);

        ImageView mPhotoEditorImageView = mPhotoEditorView.getSource();

        Glide.with(this)
                .asBitmap()
                .load(file)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        mPhotoEditorImageView.setImageBitmap(resource);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

        //Use custom font using latest support library
        Typeface mTextRobotoTf = ResourcesCompat.getFont(getContext(), R.font.roboto_medium);

        //loading font from assest
        Typeface mEmojiTypeFace = ResourcesCompat.getFont(getContext(), R.font.emojione_android);

        mPhotoEditor = new PhotoEditor.Builder(getContext(), mPhotoEditorView)
                .setPinchTextScalable(true)
                .setDefaultTextTypeface(mTextRobotoTf)
                .setDefaultEmojiTypeface(mEmojiTypeFace)
                .build();


        this.brush_button = root.findViewById(R.id.brush_button);
        this.eraser_button = root.findViewById(R.id.eraser_button);
        this.text_button = root.findViewById(R.id.text_button);
        this.filter_button = root.findViewById(R.id.filter_button);
        this.brightness_button = root.findViewById(R.id.brightness_button);
        this.emoji_button = root.findViewById(R.id.add_emoji_button);

        root.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });

        toolbar_top = root.findViewById(R.id.toolbar_top);

        toolbar_top.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.undo_btn:
                        mPhotoEditor.undo();
                        break;
                    case R.id.redo_btn:
                        mPhotoEditor.redo();
                        break;
                    case R.id.share_btn:
                        break;
                    case R.id.save_btn:
                        saveBtnAction();
                        break;
                }
                return true;
            }
        });



        //brush set up _______________________________________________________________________***

        brush_view = getLayoutInflater().inflate(R.layout.pe_brush_chooser, null);
        brush_dialog = new BottomSheetDialog(getContext());
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
        this.eraser_dialog = new BottomSheetDialog(getContext());
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
        add_text_dialog = new AlertDialog.Builder(getContext()).create();

        final int[] add_text_color = new int[1];

        final EditText add_text_input = add_text_view.findViewById(R.id.text);
        final TextView current_color = add_text_view.findViewById(R.id.current_color);
        final ColorSeekBar add_text_color_picker = add_text_view.findViewById(R.id.color_seek_bar);

        add_text_color[0] = getActivity().getColor(R.color.init_color);
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
                mPhotoEditor.addText(add_text_input.getText().toString(), add_text_color[0]);
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
        edit_text_dialog = new AlertDialog.Builder(getContext()).create();

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
        filter_dialog = new BottomSheetDialog(getContext());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        RecyclerView myList = (RecyclerView) filter_view.findViewById(R.id.filter_list);
        myList.setLayoutManager(layoutManager);
        myList.setAdapter(new PEFilterAdapter(getContext(), mPhotoEditor, PhotoFilter.values()));

        filter_dialog.setContentView(filter_view);

        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_dialog.show();
            }
        });


        //emoji set up _______________________________________________________________________***

        emoji_view = getLayoutInflater().inflate(R.layout.pe_emoji_chooser, null);
        emoji_dialog = new BottomSheetDialog(getContext());

        emoji_dialog.setContentView(emoji_view);

        int mNoOfColumns = Utility.calculateNoOfColumns(getContext(), 10f + Float.parseFloat(getString(R.string.total_emoji_width)));

        GridLayoutManager emojiLayoutManager = new GridLayoutManager(getContext(), mNoOfColumns);
        RecyclerView emojiList = emoji_view.findViewById(R.id.emoji_list);
        emojiList.setLayoutManager(emojiLayoutManager);

        ArrayList<String> emoji_unicodes = PhotoEditor.getEmojis(getContext());

        emojiList.setAdapter(new PEEmojiAdapter(emojiList, mPhotoEditor, emoji_unicodes, emoji_dialog));

        emoji_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emoji_dialog.show();

            }
        });
    }



    private void saveBtnAction() {

        mPhotoEditor.saveAsBitmap(new OnSaveBitmap() {
            @Override
            public void onBitmapReady(Bitmap bitmap) {
                if (secureFolderViewModel.resave(requireContext(), bitmap, file.getName())) {
                    NavHostFragment.findNavController(SFEditPhotoFragment.this).popBackStack();
                    Toast.makeText(getContext(), getString(R.string.save_image_success), Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getContext(), getString(R.string.save_image_fail), Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("SAVE BITMAP DEBUGGER", "onFailure: ");
                Toast.makeText(getContext(), getString(R.string.save_image_fail), Toast.LENGTH_LONG)
                        .show();
            }
        });
    }
}