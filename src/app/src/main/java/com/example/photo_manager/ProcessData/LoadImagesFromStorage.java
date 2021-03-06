package com.example.photo_manager.ProcessData;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.example.photo_manager.Format.FormatDate;
import com.example.photo_manager.Model.Date_Model;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.Type;

import java.util.ArrayList;
import java.util.Collections;

public class LoadImagesFromStorage extends AsyncTask<Void, Integer, ArrayList<Picture_Model>>{
    Context context;
    public AsyncResponse delegate = null;
    private ArrayList<Date_Model> date_models = new ArrayList<Date_Model>();

    public LoadImagesFromStorage(AsyncResponse asyncResponse,Context context){
        this.delegate = asyncResponse;
        this.context = context;
    }

    @Override
    protected ArrayList<Picture_Model> doInBackground(Void... voids) {
        ArrayList<Picture_Model> picture_models = new ArrayList<Picture_Model>();
        picture_models = loadImage();
        return picture_models;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ArrayList<Picture_Model> picture_models) {
        delegate.processPictureFinish(picture_models);
        delegate.processDateFinish(date_models);
    }

    private ArrayList<Picture_Model> loadImage(){
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        ArrayList<Picture_Model> picture_models = new ArrayList<Picture_Model>();
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_MODIFIED};

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);


        if (cursor != null) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
            int dateModifiedColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);
            while (cursor.moveToNext()) {
                Long idTmp = cursor.getLong(idColumn);
                String nameTmp = cursor.getString(nameColumn);
                int sizeTmp = cursor.getInt(sizeColumn);
                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, idTmp);
                String tmpTime = FormatDate.fullFormat.format(Long.parseLong(cursor.getString(dateModifiedColumn))*1000);
                String date = FormatDate.onlyDayFormat.format(Long.parseLong(cursor.getString(dateModifiedColumn))*1000);

                int flag = 0;
                for (int i=0;i<date_models.size();i++){
                    if(date.equals(date_models.get(i).getTime())){
                        flag++;
                    }
                }
                if (flag==0){
                    date_models.add(new Date_Model(date,Type.DATE));
                }


                picture_models.add(new Picture_Model(contentUri,nameTmp,tmpTime,sizeTmp));

            }
            cursor.close();
        }
    return picture_models;
    }

}
