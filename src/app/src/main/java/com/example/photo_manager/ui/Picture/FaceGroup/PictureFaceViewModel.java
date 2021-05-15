//package com.example.photo_manager.ui.Picture.FaceGroup;
//
//import android.app.Application;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.MutableLiveData;
//
//import java.util.List;
//
//public class PictureFaceViewModel extends AndroidViewModel {
//
//    PictureFaceReposistory reposistory;
//
//    public PictureFaceViewModel(@NonNull Application application) {
//        super(application);
//
//        reposistory = new PictureFaceReposistory(application);
//    }
//
//    public MutableLiveData<List<FaceDetectModel>> getFaceDetectModelsLiveData() {
//        return reposistory.getFaceDetectModelsLiveData();
//    }
//}
