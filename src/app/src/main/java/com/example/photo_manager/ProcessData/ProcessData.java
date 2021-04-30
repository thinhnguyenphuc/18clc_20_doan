package com.example.photo_manager.ProcessData;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;

import com.example.photo_manager.Date_Model;
import com.example.photo_manager.Format.FormatDate;
import com.example.photo_manager.Picture_Model;
import com.example.photo_manager.Type;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public abstract class ProcessData extends AppCompatActivity {
    protected String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    protected JSONObject ImageListToObject(ArrayList<Picture_Model> picture_models) throws JSONException {

        JSONObject objectList = new JSONObject();
        int size = picture_models.size();
        for (int i = 0;i<size;i++ ){
            JSONObject pic = new JSONObject();
            pic.put("uri",picture_models.get(i).getUri());
            objectList.put(String.valueOf(i),pic);
        }
        return objectList;
    }

    protected JSONObject DateListToObject(ArrayList<Date_Model> date_models) throws JSONException {
        JSONObject objectList = new JSONObject();
        int size = date_models.size();
        for (int i = 0;i<size;i++ ){
            JSONObject time = new JSONObject();
            time.put("time",date_models.get(i).getTime());
            objectList.put(String.valueOf(i),time);
        }
        return objectList;
    }
    protected void getTimeFromListPicture(ArrayList<Picture_Model> picture_models,
                                          ArrayList<Date_Model> date_models){
        for(int i=0;i<picture_models.size();i++){
            Picture_Model tmp = picture_models.get(i);
            File file = new File(getPath(tmp.getUri()));
            String tmpTime = FormatDate.onlyDayFormat.format(file.lastModified());

            ArrayList<String> tmpDate = new ArrayList<String>();
            for(int j =0 ;j<date_models.size();j++){
                tmpDate.add(date_models.get(j).getTime());
            }
            if(!tmpDate.contains(tmpTime)){
                date_models.add(new Date_Model(tmpTime, Type.DATE));
            }
        }
    }
}
