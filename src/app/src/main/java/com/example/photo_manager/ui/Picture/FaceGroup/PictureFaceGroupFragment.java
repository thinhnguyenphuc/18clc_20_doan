package com.example.photo_manager.ui.Picture.FaceGroup;

import android.media.FaceDetector;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.photo_manager.R;
//import com.google.mlkit.vision.face.Face;

import java.util.List;

public class PictureFaceGroupFragment extends Fragment {

//    PictureFaceViewModel pictureFaceViewModel;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_picture_face, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        pictureFaceViewModel = new ViewModelProvider(requireActivity()).get(PictureFaceViewModel.class);
//
//        pictureFaceViewModel.getFaceDetectModelsLiveData().observe(getViewLifecycleOwner(), new Observer<List<FaceDetectModel>>() {
//            @Override
//            public void onChanged(List<FaceDetectModel> faceDetectModels) {
//                Log.d("PICTUREFACEGROUP", "onChanged: ");
//            }
//        });
    }
}
