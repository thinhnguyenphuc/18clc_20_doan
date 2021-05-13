package com.example.photo_manager.ui.Picture;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.example.photo_manager.Adapter.Picture_Adapter_All;
import com.example.photo_manager.Model.Date_Model;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.RecyclerViewClickInterface;
import com.example.photo_manager.Code.RequestCode;
import com.example.photo_manager.Take_New_Photo;
import com.example.photo_manager.ui.ViewByDateFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PictureFragment extends Fragment implements RecyclerViewClickInterface {

    private PictureViewModel pictureViewModel;

    SharedPreferences sharedPreferences;

    ArrayList<Picture_Model> pictureModels = new ArrayList<>();
    ArrayList<Date_Model> dateModels = new ArrayList<>();

    private RecyclerView recyclerView;
    private Picture_Adapter_All picture_adapter_all;

    NavController navController = null;

    Toolbar toolbar;

    Toolbar toolbar_delete;
    CheckBox selectAllCheckBox;

    private ViewByDateFragment viewByDate;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireActivity()
                .getSharedPreferences("com.example.photo_manager.settings", Context.MODE_PRIVATE);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_picture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        toolbar = root.findViewById(R.id.toolbar_top);
        toolbar_delete = root.findViewById((R.id.toolbar_delete_top));

        selectAllCheckBox = root.findViewById(R.id.select_all_check_box);

        navController = NavHostFragment.findNavController(this);

        BottomNavigationView bnv = requireActivity().findViewById(R.id.nav_view);
        bnv.setVisibility(View.VISIBLE);

        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.favourite:
                                navController.navigate(R.id.action_pictureFragment_to_favouriteFragment);
                                break;
                            case R.id.camera:
                                startActivityForResult(new Intent(requireActivity(), Take_New_Photo.class), RequestCode.REQUEST_INTENT_TAKE_NEW_PHOTO);
                                break;
                            case R.id.slideshow: {
                                PictureFragmentDirections.ActionPictureFragmentToSlideShowFragment action
                                        = PictureFragmentDirections.actionPictureFragmentToSlideShowFragment(-1);
                                navController.navigate(action);
                                break;
                            }
                            case R.id.secure_folder: {
                                SharedPreferences sharedPreferences = requireActivity()
                                        .getSharedPreferences("com.example.photo_manager.ui.SecurityFolder", Context.MODE_PRIVATE);

                                String password = sharedPreferences.getString("password", "");

                                if (sharedPreferences.getString("password", "").isEmpty()) {
                                    NavDirections action = PictureFragmentDirections.actionPictureFragmentToSFFirstAccessFragment();
                                    navController.navigate(action);
                                } else {
                                    NavDirections action = PictureFragmentDirections.actionPictureFragmentToSFPasswordFragment();
                                    navController.navigate(action);
                                }

                                break;
                            }
                            case R.id.view_by_date: {

                                viewByDate = new ViewByDateFragment();
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(PictureFragment.this.getId(), viewByDate);



                                Bundle bundle = new Bundle();
                                try {
                                    bundle.putString("imageLists", ImageListToObject().toString());
                                    bundle.putString("dateLists", DateListToObject().toString());
                                    bundle.putInt("sizeOfPicture",pictureModels.size());
                                    bundle.putInt("sizeOfDate",dateModels.size());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                viewByDate.setArguments(bundle);

                                fragmentTransaction.commit();
                            }
                        }
                        return true;
                    }
                });

        recyclerView = root.findViewById(R.id.recyclerView_ViewAll);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        picture_adapter_all = new Picture_Adapter_All(getContext(),this);
        recyclerView.setAdapter(picture_adapter_all);


        pictureViewModel =
                new ViewModelProvider(requireActivity()).get(PictureViewModel.class);
        final SkeletonScreen skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(picture_adapter_all)
                .shimmer(true)
                .angle(20)
                .frozen(false)
                .duration(1200)
                .count(40)
                .load(R.layout.item_skeleton_news)
                .show(); //default count is 10

        pictureViewModel.getAllPictures().observe(getViewLifecycleOwner(), new Observer<ArrayList<Picture_Model>>() {
            @Override
            public void onChanged(ArrayList<Picture_Model> picture_models) {
                if(picture_models.size()>0){
                    skeletonScreen.hide();
                }
                PictureFragment.this.pictureModels = picture_models;
                picture_adapter_all.setPictures(picture_models);
            }

        });
        pictureViewModel.getAllDates().observe(getViewLifecycleOwner(), new Observer<ArrayList<Date_Model>>() {
            @Override
            public void onChanged(ArrayList<Date_Model> date_models) {
                dateModels = date_models;
            }
        });

        picture_adapter_all.setMultiChoiceSelectionListener(new MultiChoiceAdapter.Listener() {
            @Override
            public void OnItemSelected(int selectedPosition, int itemSelectedCount, int allItemCount) {
                if (toolbar_delete.getVisibility() == View.INVISIBLE) {
                    toolbar_delete.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.INVISIBLE);
                    requireActivity().findViewById(R.id.nav_view).setVisibility(View.INVISIBLE);
                    picture_adapter_all.setSingleClickMode(true);
                }
            }

            @Override
            public void OnItemDeselected(int deselectedPosition, int itemSelectedCount, int allItemCount) {
            }
            @Override
            public void OnSelectAll(int itemSelectedCount, int allItemCount) {
            }
            @Override
            public void OnDeselectAll(int itemSelectedCount, int allItemCount) {
            }
        });

        root.findViewById(R.id.back_from_delete_button).setOnClickListener(v -> {
            this.returnFromDeleteMode();
        });

        toolbar_delete.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.delete: {

                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle(R.string.multiple_deletion);
                    alertDialog.setMessage(getString(R.string.multiple_deletion_message));
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getContext().getString(R.string.OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            ArrayList<Picture_Model> deletePictureModels = new ArrayList<>();
                            for (Integer i : picture_adapter_all.getSelectedItemList()) {
                                deletePictureModels.add(pictureModels.get(i));
                            }
                            deletePictures(getContext(), deletePictureModels);
                            Toast.makeText(getContext(), R.string.delete_image_success, Toast.LENGTH_LONG).show();
                        }
                    });
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getContext().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alertDialog.show();
                }
            }
            return true;
        });

        root.findViewById(R.id.select_all_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAllCheckBox.setChecked(!selectAllCheckBox.isChecked());
            }
        });

        selectAllCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    picture_adapter_all.selectAll();
                } else {
                    picture_adapter_all.deselectAll();
                }
            }
        });

    }

    public void returnFromDeleteMode() {
        toolbar_delete.setVisibility(View.INVISIBLE);
        toolbar.setVisibility(View.VISIBLE);
        requireActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
        picture_adapter_all.deselectAll();
        picture_adapter_all.setSingleClickMode(false);
    }

    @Override
    public void onItemClick(int position) {
        PictureFragmentDirections.ActionPictureFragmentToViewPhotoFragment action =
                PictureFragmentDirections.actionPictureFragmentToViewPhotoFragment(pictureModels.get(position).getUri().toString());
        navController.navigate(action);
    }

    @Override
    public void onLongItemClick(int position) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("my debugger", "on fragment result: ");
        if (requestCode == RequestCode.REQUEST_INTENT_TAKE_NEW_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                String test = data.getStringExtra("uri");
                Uri tmp = Uri.parse(test);
                pictureViewModel.updateTakeNewPhoto(getContext(), new Picture_Model(tmp,null,null,0));
            }
        }
    }

    public void deletePictures(Context context, ArrayList<Picture_Model> picture_models) {
        pictureViewModel.deleteImages(context, picture_models);
    }

    private JSONObject ImageListToObject() throws JSONException {

        JSONObject objectList = new JSONObject();
        int size = this.pictureModels.size();
        for (int i = 0;i<size;i++ ){
            JSONObject pic = new JSONObject();
            pic.put("uri",this.pictureModels.get(i).getUri());
            pic.put("time",this.pictureModels.get(i).getTime());
            objectList.put(String.valueOf(i),pic);
        }
        return objectList;
    }

    private JSONObject DateListToObject() throws JSONException {
        JSONObject objectList = new JSONObject();
        int size = this.dateModels.size();
        for (int i = 0;i<size;i++ ){
            JSONObject time = new JSONObject();
            time.put("time",this.dateModels.get(i).getTime());
            objectList.put(String.valueOf(i),time);
        }
        return objectList;
    }
    

}