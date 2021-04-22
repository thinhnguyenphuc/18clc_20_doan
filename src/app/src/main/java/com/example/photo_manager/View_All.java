package com.example.photo_manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.photo_manager.Adapter.Picture_Adapter_All;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class View_All extends AppCompatActivity implements RecyclerViewClickInterface{


    ArrayList<Picture_Model> picture_models = new ArrayList<Picture_Model>();
    private RecyclerView recyclerView;
    private Picture_Adapter_All picture_adapter_all ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__all);

        Intent receiver  = getIntent();

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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView = findViewById(R.id.recyclerView_ViewAll);
        picture_adapter_all = new Picture_Adapter_All(this,picture_models,this);
        recyclerView.setAdapter(picture_adapter_all);
        recyclerView.setLayoutManager(new GridLayoutManager(this,5));
    }

    @Override
    public void onItemClick(int position) {
        Intent view_photo = new Intent(View_All.this, ViewPhoto.class);
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