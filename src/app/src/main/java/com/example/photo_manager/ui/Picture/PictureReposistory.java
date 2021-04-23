package com.example.photo_manager.ui.Picture;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.photo_manager.Date_Model;
import com.example.photo_manager.PEAdapters.Utility;
import com.example.photo_manager.Picture_Model;
import com.example.photo_manager.Type;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PictureReposistory {
    private static final SimpleDateFormat fullFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
    private static final SimpleDateFormat onlyDayFormat = new SimpleDateFormat("dd-MM-yyyy");
    ArrayList<Picture_Model> picture_models = new ArrayList<Picture_Model>();
    ArrayList<Date_Model> date_models = new ArrayList<Date_Model>();

    MutableLiveData<ArrayList<Picture_Model>> pictures = new MutableLiveData<>();
    MutableLiveData<ArrayList<Date_Model>> dates  = new MutableLiveData<>();

    public PictureReposistory(Context context) {

        loadImage(context);
        pictures.setValue(picture_models);
        dates.setValue(date_models);
    }

    public MutableLiveData<ArrayList<Picture_Model>> getAllPictures() {
        return pictures;
    }

    public  MutableLiveData<ArrayList<Date_Model>> getAllDates() {
        return dates;
    }

    public void delete(Uri uri) {
        Log.d("my debugger", "before delete: ");
        for (Picture_Model picture_model: picture_models) {

            if (picture_model.getUri().equals(uri)) {
                picture_models.remove(picture_model);
                pictures.setValue(picture_models);
                Log.d("my debugger", "delete: ");
                return;
            }
        }
    }

    public void notifyDataChanged() {
        this.pictures.setValue(picture_models);
    }

    public void update(Context context) {
        loadImage(context);
        this.pictures.setValue(picture_models);
    }


    private void loadImage(Context context){
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        picture_models.clear();
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};


        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);


        if (cursor != null) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
            while (cursor.moveToNext()) {
                Long idTmp = cursor.getLong(idColumn);
                String nameTmp = cursor.getString(nameColumn);
                int sizeTmp = cursor.getInt(sizeColumn);
                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, idTmp);
                File file = new File(getPath(context, contentUri));
                String tmpTime =  fullFormat.format(file.lastModified());


                this.picture_models.add(new Picture_Model(contentUri,nameTmp,tmpTime,sizeTmp));

            }
            cursor.close();
        }
    }

    private void getTimeFromListPicture(Context context){
        for(int i=0;i<picture_models.size();i++){
            Picture_Model tmp = picture_models.get(i);
            File file = new File(getPath(context, tmp.getUri()));
            String tmpTime =  onlyDayFormat.format(file.lastModified());

            ArrayList<String> tmpDate = new ArrayList<String>();
            for(int j =0 ;j<date_models.size();j++){
                tmpDate.add(date_models.get(j).getTime());
            }
            if(!tmpDate.contains(tmpTime)){
                date_models.add(new Date_Model(tmpTime, Type.DATE));
            }
        }
    }


    public String getPath(Context context, Uri uri) {
        return Utility.getRealPathFromUri(context, uri);
    }

    private JSONObject ImageListToObject() throws JSONException {

        JSONObject objectList = new JSONObject();
        int size = this.picture_models.size();
        for (int i = 0;i<size;i++ ){
            JSONObject pic = new JSONObject();
            pic.put("uri",this.picture_models.get(i).getUri());
            objectList.put(String.valueOf(i),pic);
        }
        return objectList;
    }

    private JSONObject DateListToObject() throws JSONException {
        JSONObject objectList = new JSONObject();
        int size = this.date_models.size();
        for (int i = 0;i<size;i++ ){
            JSONObject time = new JSONObject();
            time.put("time",this.date_models.get(i).getTime());
            objectList.put(String.valueOf(i),time);
        }
        return objectList;
    }
}
