package com.example.photo_manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.example.photo_manager.Adapter.Picture_Adapter;
import com.example.photo_manager.Code.RequestCode;
import com.example.photo_manager.Model.Date_Model;
import com.example.photo_manager.Model.Picture_Model;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class View_By_Date extends AppCompatActivity implements RecyclerViewClickInterface {
    private static final SimpleDateFormat fullFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
    private RecyclerView recyclerView;
    private Picture_Adapter picture_adapter ;
    ArrayList data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__by__date);

        Intent receiver = getIntent();

        receiverData(receiver,this.data);


        recyclerView = findViewById(R.id.recyclerView);
        picture_adapter = new Picture_Adapter(this,data,this );

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getApplicationContext());
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        recyclerView.setLayoutManager(flexboxLayoutManager);

        recyclerView.setAdapter(picture_adapter);



    }

    public void receiverData(Intent receiver, ArrayList data){
        ArrayList<Picture_Model> picture_models = new ArrayList<Picture_Model>();
        ArrayList<Date_Model> date_models = new ArrayList<Date_Model>();
        int sizeOfPicture = receiver.getIntExtra("sizeOfPicture",0);

        try {
            JSONObject tmpListObject = new JSONObject(receiver.getStringExtra("images"));
            for(int i = 0 ;i<sizeOfPicture;i++){
                JSONObject tmpObject = new JSONObject(tmpListObject.getString(String.valueOf(i)));
                Uri tmpUri = Uri.parse(tmpObject.get("uri").toString());
                File file = new File(getPath(tmpUri));
                String tmpTime =  fullFormat.format(file.lastModified());
                picture_models.add(new Picture_Model(tmpUri,null,tmpTime,0));
            }
        } catch (JSONException  e) {
            e.printStackTrace();
        }

        int sizeOfDate = receiver.getIntExtra("sizeOfDate",0);
        try {
            JSONObject tmpListObject = new JSONObject(receiver.getStringExtra("time"));
            for(int i = 0 ;i<sizeOfDate;i++){
                JSONObject tmpObject = new JSONObject(tmpListObject.getString(String.valueOf(i)));
                String tmpTime = tmpObject.get("time").toString();

                date_models.add(new Date_Model(tmpTime,Type.DATE));
            }
        } catch (JSONException  e) {
            e.printStackTrace();
        }

        toArrayList(date_models,picture_models,data);
    }

    private void toArrayList(ArrayList<Date_Model> date_models
            ,ArrayList<Picture_Model> picture_models,ArrayList data) {
        for(int i=0;i<date_models.size();i++){
            data.add(date_models.get(i));
            for (int j=0;j<picture_models.size();j++){
                if(picture_models.get(j).getTime().contains(date_models.get(i).getTime())){
                    data.add(picture_models.get(j));
                }
            }
        }
    }

    @Override
    public void onItemClick(int position) {

        Picture_Model picture_model = (Picture_Model) data.get(position);

        Intent view_photo = new Intent(View_By_Date.this, ViewPhoto.class);
        view_photo.putExtra("uri",picture_model.getUri().toString());
        view_photo.putExtra("name",picture_model.getName());
        view_photo.putExtra("time",picture_model.getTime());
        view_photo.putExtra("size",picture_model.getSize());
        startActivityForResult(view_photo, RequestCode.REQUEST_INTENT_VIEW_PHOTO);
    }

    @Override
    public void onLongItemClick(int position) {

    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}