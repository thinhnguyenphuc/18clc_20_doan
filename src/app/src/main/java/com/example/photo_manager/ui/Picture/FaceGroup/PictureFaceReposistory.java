//package com.example.photo_manager.ui.Picture.FaceGroup;
//
//import android.app.Application;
//import android.content.ContentUris;
//import android.content.Context;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.provider.MediaStore;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.LifecycleOwner;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.lifecycle.ViewModelStoreOwner;
//
//import com.example.photo_manager.ui.Picture.PictureViewModel;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.mlkit.vision.common.InputImage;
//import com.google.mlkit.vision.face.Face;
//import com.google.mlkit.vision.face.FaceDetection;
//import com.google.mlkit.vision.face.FaceDetector;
//import com.google.mlkit.vision.face.FaceDetectorOptions;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class PictureFaceReposistory {
//
//    MutableLiveData<List<FaceDetectModel>> faceDetectModelsLiveData = new MutableLiveData<>();
//    List<FaceDetectModel> faceDetectModels;
//
//    public PictureFaceReposistory(Application application) {
//        new LoadAsynTask(application).execute();
//    }
//
//    public MutableLiveData<List<FaceDetectModel>> getFaceDetectModelsLiveData() {
//        return faceDetectModelsLiveData;
//    }
//
//    private class LoadAsynTask extends AsyncTask<Void, Void, Void> {
//
//        Context context;
//        public LoadAsynTask(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            loadData(context);
//            return null;
//        }
//    }
//
//    private void loadData(Context context) {
//
//        FaceDetectorOptions options =  new FaceDetectorOptions.Builder()
//                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
//                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
//                .enableTracking()
//                .build();
//
//        ArrayList<Uri> uris = loadImage(context);
//
//        faceDetectModels = new ArrayList<>();
//
//        for (Uri uri: uris) {
//            faceDetectModels.add(new FaceDetectModel(uri));
//        }
//
//        FaceDetector detector = FaceDetection.getClient(options);
//
//        for (FaceDetectModel faceDetectModel: faceDetectModels) {
//            InputImage image;
//            try {
//                image = InputImage.fromFilePath(context, faceDetectModel.getUri());
//
//                Task<List<Face>> result =
//                        detector.process(image)
//                                .addOnSuccessListener(
//                                        new OnSuccessListener<List<Face>>() {
//                                            @Override
//                                            public void onSuccess(List<Face> faces) {
//                                                faceDetectModel.setFaces(faces);
//                                                faceDetectModelsLiveData.setValue(faceDetectModels);
//                                            }
//                                        })
//                                .addOnFailureListener(
//                                        new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                // Task failed with an exception
//                                                // ...
//                                            }
//                                        });
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private ArrayList<Uri> loadImage(Context context){
//        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//
//        ArrayList<Uri> uris = new ArrayList<>();
//        String[] projection = {
//                MediaStore.Images.Media._ID,
//                MediaStore.Images.Media.DISPLAY_NAME,
//                MediaStore.Images.Media.SIZE,
//                MediaStore.Images.Media.DATE_MODIFIED};
//
//        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
//
//
//        if (cursor != null) {
//            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
//            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
//            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
//            int dateModifiedColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);
//            while (cursor.moveToNext()) {
//                Long idTmp = cursor.getLong(idColumn);
//                String nameTmp = cursor.getString(nameColumn);
//                int sizeTmp = cursor.getInt(sizeColumn);
//                Uri contentUri = ContentUris.withAppendedId(
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, idTmp);
//
//                uris.add(contentUri);
//
//            }
//            cursor.close();
//        }
//        return uris;
//    }
//}
