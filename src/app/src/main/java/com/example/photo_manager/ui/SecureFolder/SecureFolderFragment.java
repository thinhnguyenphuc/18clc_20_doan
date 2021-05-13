package com.example.photo_manager.ui.SecureFolder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter;
import com.example.photo_manager.R;
import com.example.photo_manager.Utility;
import com.example.photo_manager.ui.SecureFolder.SecureFolderViewModel.SecureFolderViewModel;

import java.io.File;
import java.util.List;

public class SecureFolderFragment extends Fragment {

    private SecureFolderViewModel secureFolderViewModel;
    private NavController navController;

    private RecyclerView recyclerView;
    private SecureFolderAdapter adapter;

    Toolbar toolbar;
    Toolbar toolbar_delete;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_secure_folder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        secureFolderViewModel = new ViewModelProvider(requireActivity()).get(SecureFolderViewModel.class);
        navController = Navigation.findNavController(view);

        toolbar = view.findViewById(R.id.toolbar_top);
        toolbar_delete = view.findViewById((R.id.toolbar_delete_top));
        
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.change_password:
                    changePassword();
            }
            return true;
        });

        toolbar_delete.setOnMenuItemClickListener(item -> {
            switch(item.getItemId()) {
                case R.id.delete: {
                    deleteSelectedItem();
                }
            }
            return true;
        });

        this.recyclerView = view.findViewById(R.id.recyclerView);

        this.adapter = new SecureFolderAdapter(requireContext(), navController, secureFolderViewModel);

        int picture_width = (int) (getResources().getDimension(R.dimen.picture_width) / getResources().getDisplayMetrics().density);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), Utility.calculateNoOfColumns(getContext(), picture_width));

        this.recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        secureFolderViewModel.getFiles().observe(getViewLifecycleOwner(), new Observer<List<File>>() {
            @Override
            public void onChanged(List<File> files) {
                adapter.setData(files);
            }
        });


        adapter.setMultiChoiceSelectionListener(new MultiChoiceAdapter.Listener() {
            @Override
            public void OnItemSelected(int selectedPosition, int itemSelectedCount, int allItemCount) {
                if (toolbar_delete.getVisibility() == View.INVISIBLE) {
                    toolbar_delete.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.INVISIBLE);
                    requireActivity().findViewById(R.id.nav_view).setVisibility(View.INVISIBLE);
                    adapter.setSingleClickMode(true);
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

        view.findViewById(R.id.back_from_delete_button).setOnClickListener(v -> {
            this.returnFromDeleteMode();
        });

        view.findViewById(R.id.back_button).setOnClickListener(v -> {
            NavDirections action = SecureFolderFragmentDirections.actionSecurityFolderFragmentToMediaFragment();
            navController.navigate(action);
        });

    }

    private void deleteSelectedItem() {
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
        alertDialog.setTitle(getString(R.string.multiple_deletion));
        alertDialog.setMessage(getString(R.string.multiple_deletion_message));
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, requireContext().getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                adapter.deleteSelectedItem();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, requireContext().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();
    }

    private void returnFromDeleteMode() {
        toolbar_delete.setVisibility(View.INVISIBLE);
        toolbar.setVisibility(View.VISIBLE);
        requireActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
        adapter.deselectAll();
        adapter.setSingleClickMode(false);
    }


    private void changePassword() {
        NavDirections action = SecureFolderFragmentDirections.actionSecurityFolderFragmentToSFChangePasswordFragment();
        navController.navigate(action);
    }
}