package com.example.photo_manager.ui;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.photo_manager.Adapter.View_By_Date_Picture_Adaper;
import com.example.photo_manager.Format.FormatDate;
import com.example.photo_manager.Model.Date_Model;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.RecyclerViewClickInterface;
import com.example.photo_manager.Type;
import com.example.photo_manager.Utility;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;


public class ViewByDateFragment extends Fragment implements RecyclerViewClickInterface {


    ArrayList data =new ArrayList<>();
    private RecyclerView recyclerView;
    private View_By_Date_Picture_Adaper viewByDatePicture_adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_view_by_date, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();

        if (bundle!=null){ dataReceiveToList(bundle); }

        recyclerView = view.findViewById(R.id.viewByDate_recyclerView);
        viewByDatePicture_adapter = new View_By_Date_Picture_Adaper(getContext(),data,this );

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        recyclerView.setLayoutManager(flexboxLayoutManager);

        recyclerView.setAdapter(viewByDatePicture_adapter);
    }

    public void dataReceiveToList(Bundle bundle){
        ArrayList<Picture_Model> picture_models = new ArrayList<Picture_Model>();
        ArrayList<Date_Model> date_models = new ArrayList<Date_Model>();
        int sizeOfPicture = bundle.getInt("sizeOfPicture",0);

        try {
            JSONObject tmpListObject = new JSONObject(bundle.getString("imageLists"));
            for(int i = 0 ;i<sizeOfPicture;i++){
                JSONObject tmpObject = new JSONObject(tmpListObject.getString(String.valueOf(i)));
                Uri tmpUri = Uri.parse(tmpObject.get("uri").toString());
                File file = new File(Utility.getRealPathFromUri(getContext(),tmpUri));
                String tmpTime = FormatDate.fullFormat.format(file.lastModified());
                picture_models.add(new Picture_Model(tmpUri,null,tmpTime,0));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int sizeOfDate = bundle.getInt("sizeOfDate",0);
        try {
            JSONObject tmpListObject = new JSONObject(bundle.getString("dateLists"));
            for(int i = 0 ;i<sizeOfDate;i++){
                JSONObject tmpObject = new JSONObject(tmpListObject.getString(String.valueOf(i)));
                String tmpTime = tmpObject.get("time").toString();

                date_models.add(new Date_Model(tmpTime, Type.DATE));
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

    }

    @Override
    public void onLongItemClick(int position) {

    }
}