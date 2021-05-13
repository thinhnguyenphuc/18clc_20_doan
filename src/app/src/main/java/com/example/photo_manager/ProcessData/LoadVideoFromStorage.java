package com.example.photo_manager.ProcessData;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.example.photo_manager.Format.FormatDate;
import com.example.photo_manager.Model.Date_Model;
import com.example.photo_manager.Model.Video_Model;
import com.example.photo_manager.Type;

import java.util.ArrayList;

public class LoadVideoFromStorage extends AsyncTask<Void, Integer, ArrayList<Video_Model>> {

    Context context;
    AsyncResponse asyncResponse;
    private ArrayList<Date_Model> date_models = new ArrayList<Date_Model>();
    public LoadVideoFromStorage(AsyncResponse asyncResponse, Context context){
        this.asyncResponse = asyncResponse;
        this.context = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Video_Model> doInBackground(Void... voids) {

        ArrayList<Video_Model> video_models = new ArrayList<Video_Model>();
        video_models = loadVideo();
        return video_models;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ArrayList<Video_Model> video_models) {
        asyncResponse.processVideoFinish(video_models);
        asyncResponse.processDateFinish(date_models);
    }

    private ArrayList<Video_Model> loadVideo(){
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        ArrayList<Video_Model> video_models = new ArrayList<Video_Model>();
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.DURATION};

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);


        if (cursor != null) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
            int dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED);

            while (cursor.moveToNext()) {
                Long idTmp = cursor.getLong(idColumn);
                String nameTmp = cursor.getString(nameColumn);
                int sizeTmp = cursor.getInt(sizeColumn);
                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, idTmp);
                String tmpTime = FormatDate.fullFormat.format(Long.parseLong(cursor.getString(dateModifiedColumn))*1000);
                String date = FormatDate.onlyDayFormat.format(Long.parseLong(cursor.getString(dateModifiedColumn))*1000);

                video_models.add(new Video_Model(contentUri,nameTmp,tmpTime,sizeTmp,0));

                int flag = 0;
                for (int i=0;i<date_models.size();i++){
                    if(date.equals(date_models.get(i).getTime())){
                        flag++;
                    }
                }
                if (flag==0){
                    date_models.add(new Date_Model(date, Type.DATE));
                }

            }
            cursor.close();
        }
        return video_models;
    }
    private int getDuration(Uri uri){
        MediaPlayer mp = MediaPlayer.create(context, uri);
        int duration = mp.getDuration();
        mp.release();
        return duration;
    }
}
