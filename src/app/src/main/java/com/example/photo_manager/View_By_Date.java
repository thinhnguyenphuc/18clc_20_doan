package com.example.photo_manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.Call;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class View_By_Date extends AppCompatActivity implements RecyclerViewClickInterface {

    ArrayList<Picture_Model> picture_models = new ArrayList<Picture_Model>();
    private RecyclerView recyclerView;
    private Picture_Adapter picture_adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__by__date);

        Intent receiver = getIntent();
        int size = receiver.getIntExtra("size",0);

        try {
            JSONObject tmpListObject = new JSONObject(receiver.getStringExtra("images"));
            for(int i = 0 ;i<size;i++){
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

        recyclerView = findViewById(R.id.recyclerView);
        picture_adapter = new Picture_Adapter(this,picture_models,this );
        recyclerView.setAdapter(picture_adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));



    }

    @Override
    public void onItemClick(int position) {
        Intent view_photo = new Intent(View_By_Date.this, ViewPhoto.class);
        view_photo.putExtra("uri",picture_models.get(position).getUri().toString());
        view_photo.putExtra("name",picture_models.get(position).getName());
        view_photo.putExtra("time",picture_models.get(position).getTime());
        view_photo.putExtra("size",picture_models.get(position).getSize());
        startActivityForResult(view_photo,RequestCode.REQUEST_INTENT_VIEW_PHOTO);
    }

    @Override
    public void onLongItemClick(int position) {

    }
}