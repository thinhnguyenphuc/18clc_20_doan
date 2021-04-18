package com.example.photo_manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class View_By_Date extends AppCompatActivity {

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
                int tmpSize = Integer.parseInt(tmpObject.get("size").toString());
                picture_models.add(new Picture_Model(tmpUri,tmpName,tmpSize));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ImageView test = (ImageView)findViewById(R.id.imageTest);
        Toast.makeText(this, picture_models.get(1).getUri().toString(), Toast.LENGTH_LONG).show();
        Glide.with(this).load(picture_models.get(1).getUri()).into(test);

        //recyclerView = findViewById(R.id.recyclerPicture);
        //picture_adapter = new Picture_Adapter(this,picture_models);
        //recyclerView.setAdapter(picture_adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}