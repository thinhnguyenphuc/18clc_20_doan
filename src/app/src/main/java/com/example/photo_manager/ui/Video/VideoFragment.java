package com.example.photo_manager.ui.Video;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.example.photo_manager.Adapter.Video_Adapter_All;
import com.example.photo_manager.Code.RequestCode;
import com.example.photo_manager.Model.Date_Model;
import com.example.photo_manager.Model.Picture_Model;
import com.example.photo_manager.Model.Video_Model;
import com.example.photo_manager.R;
import com.example.photo_manager.RecyclerViewClickInterface;
import com.example.photo_manager.Take_New_Photo;
import com.example.photo_manager.Take_New_Video;
import com.example.photo_manager.Utility;
import com.example.photo_manager.ui.Picture.PictureFragment;
import com.example.photo_manager.ui.ViewByDateFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class VideoFragment extends Fragment implements RecyclerViewClickInterface {

    private VideoViewModel videoViewModel;

    private ArrayList<Video_Model> videoModels = new ArrayList<>();
    private ArrayList<Date_Model> dateModels = new ArrayList<>();
    private RecyclerView recyclerView;
    private Video_Adapter_All video_adapter_all;

    private boolean linear_layout_flag = false;

    NavController navController = null;

    Toolbar toolbar;
    private ViewByDateFragment viewByDate;
    Toolbar toolbar_delete;
    CheckBox selectAllCheckBox;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.video_fragment, container, false);

        toolbar = root.findViewById(R.id.toolbar_top);

        navController = NavHostFragment.findNavController(this);

        BottomNavigationView bnv = requireActivity().findViewById(R.id.nav_view);
        bnv.setVisibility(View.VISIBLE);

        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.favourite:
                                NavDirections action = VideoFragmentDirections.actionVideoFragmentToFavouriteFragment();
                                navController.navigate(action);
                                break;
                            case R.id.camera:
                                startActivityForResult(new Intent(requireActivity(), Take_New_Video.class), RequestCode.REQUEST_INTENT_TAKE_NEW_VIDEO);
                                break;
                            case R.id.view_by_date: {

                                viewByDate = new ViewByDateFragment();
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(VideoFragment.this.getId(), viewByDate);

                                SortDate();


                                Bundle bundle = new Bundle();
                                try {
                                    bundle.putString("type","video");
                                    bundle.putString("videoLists", VideoListToObject().toString());
                                    bundle.putString("dateLists", DateListToObject().toString());
                                    bundle.putInt("sizeOfVideo",videoModels.size());
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

        recyclerView = root.findViewById(R.id.recyclerView_ViewAllVideo);

        video_adapter_all = new Video_Adapter_All(getContext(),this);
        setAdapter(linear_layout_flag);


        recyclerView.setAdapter(video_adapter_all);

        videoViewModel =
                new ViewModelProvider(requireActivity()).get(VideoViewModel.class);
        final SkeletonScreen skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(video_adapter_all)
                .shimmer(true)
                .angle(20)
                .frozen(false)
                .duration(1200)
                .count(40)
                .load(R.layout.item_skeleton_news)
                .show();

        videoViewModel.getAllVideos().observe(getViewLifecycleOwner(), new Observer<ArrayList<Video_Model>>() {
            @Override
            public void onChanged(ArrayList<Video_Model> video_models) {
                skeletonScreen.hide();
                videoModels = video_models;
                video_adapter_all.setVideos(video_models);
            }

        });
        videoViewModel.getAllDates().observe(getViewLifecycleOwner(), new Observer<ArrayList<Date_Model>>() {
            @Override
            public void onChanged(ArrayList<Date_Model> date_models) {
                dateModels = date_models;
            }
        });
        return root;

    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        selectAllCheckBox = root.findViewById(R.id.select_all_check_box);
        toolbar_delete = root.findViewById((R.id.toolbar_delete_top));
        video_adapter_all.setMultiChoiceSelectionListener(new MultiChoiceAdapter.Listener() {
            @Override
            public void OnItemSelected(int selectedPosition, int itemSelectedCount, int allItemCount) {
                if (toolbar_delete.getVisibility() == View.INVISIBLE) {
                    toolbar_delete.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.INVISIBLE);
                    requireActivity().findViewById(R.id.nav_view).setVisibility(View.INVISIBLE);
                    video_adapter_all.setSingleClickMode(true);
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

        root.findViewById(R.id.select_all_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAllCheckBox.setChecked(!selectAllCheckBox.isChecked());
            }
        });

        root.findViewById(R.id.back_from_delete_button).setOnClickListener(v -> {
            this.returnFromDeleteMode();
        });
        selectAllCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (linear_layout_flag) {
                    } else {
                        video_adapter_all.selectAll();
                    }
                } else {
                    if (linear_layout_flag) {
                    } else {
                        video_adapter_all.deselectAll();
                    }
                }
            }
        });
    }

    public void returnFromDeleteMode() {
        toolbar_delete.setVisibility(View.INVISIBLE);
        toolbar.setVisibility(View.VISIBLE);
        requireActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
        video_adapter_all.deselectAll();
        video_adapter_all.setSingleClickMode(false);

    }


    @Override
    public void onItemClick(int position) {
        VideoFragmentDirections.ActionVideoFragmentToViewVideo action =
                VideoFragmentDirections.actionVideoFragmentToViewVideo(videoModels.get(position).getUri().toString());
        navController.navigate(action);

    }

    @Override
    public void onLongItemClick(int position) {

    }
    private void SortDate() {
        Collections.sort(dateModels, new Comparator<Date_Model>() {
            @Override
            public int compare(Date_Model o1, Date_Model o2) {
                String tmp1[] = o1.getTime().split("-");
                String tmp2[] = o2.getTime().split("-");
                if(Integer.parseInt(tmp1[2])>Integer.parseInt(tmp2[2])){
                    return -1;
                } else if(Integer.parseInt(tmp1[1])>Integer.parseInt(tmp2[1])) {
                    return -1;
                } else if(Integer.parseInt(tmp1[0])>Integer.parseInt(tmp2[0])) {
                    return -1;
                }
                return 1;
            }
        });
    }

    private JSONObject VideoListToObject() throws JSONException {

        JSONObject objectList = new JSONObject();
        int size = this.videoModels.size();
        for (int i = 0;i<size;i++ ){
            JSONObject pic = new JSONObject();
            pic.put("uri",this.videoModels.get(i).getUri());
            pic.put("time",this.videoModels.get(i).getTime());
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

    private void setAdapter(boolean linear) {
        recyclerView.setAdapter(video_adapter_all);
        int dp = (int) (getResources().getDimension(R.dimen.picture_width) / getResources().getDisplayMetrics().density);
        int spanCount = Utility.calculateNoOfColumns(requireContext(), dp);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("my debugger", "on fragment result: ");
        if (requestCode == RequestCode.REQUEST_INTENT_TAKE_NEW_VIDEO) {
            if (resultCode == Activity.RESULT_OK) {
                String test = data.getStringExtra("uri");
                Uri tmp = Uri.parse(test);
                videoViewModel.updateTakeNewPhoto(getContext(), new Video_Model(tmp,null,null,0,0));
            }
        }
    }
}