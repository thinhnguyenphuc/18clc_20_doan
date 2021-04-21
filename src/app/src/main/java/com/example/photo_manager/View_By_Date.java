package com.example.photo_manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.Call;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class View_By_Date extends AppCompatActivity implements RecyclerViewClickInterface {

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

        recyclerView.setAdapter(picture_adapter);
        recyclerView.setLayoutManager(flexboxLayoutManager);



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
                String tmpName = tmpObject.get("name").toString();
                String tmpTime = tmpObject.get("time").toString();
                int tmpSize = Integer.parseInt(tmpObject.get("size").toString());

                picture_models.add(new Picture_Model(tmpUri,tmpName,tmpTime,tmpSize));
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
            String tmpDate = date_models.get(i).getTime();
            for (int j=0;j<picture_models.size();j++){
                String tmpPic  = picture_models.get(j).getTime();
                if(picture_models.get(j).getTime().contains(date_models.get(i).getTime())){
                    data.add(picture_models.get(j));
                }
            }
        }
    }

    @Override
    public void onItemClick(int position) {
//        Intent view_photo = new Intent(View_By_Date.this, ViewPhoto.class);
//        view_photo.putExtra("uri",picture_models.get(position).getUri().toString());
//        view_photo.putExtra("name",picture_models.get(position).getName());
//        view_photo.putExtra("time",picture_models.get(position).getTime());
//        view_photo.putExtra("size",picture_models.get(position).getSize());
//        startActivityForResult(view_photo,RequestCode.REQUEST_INTENT_VIEW_PHOTO);
    }

    @Override
    public void onLongItemClick(int position) {

    }
}