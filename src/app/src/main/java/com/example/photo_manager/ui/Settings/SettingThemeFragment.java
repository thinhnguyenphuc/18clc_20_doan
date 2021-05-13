package com.example.photo_manager.ui.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.photo_manager.R;


public class SettingThemeFragment extends Fragment {

    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireActivity()
                .getSharedPreferences("com.example.photo_manager.settings", Context.MODE_PRIVATE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_theme, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        view.findViewById(R.id.theme).setOnClickListener(v -> {
            Log.d("THEME", "onViewCreated: ");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("theme", 0);
            editor.apply();
            Navigation.findNavController(view).popBackStack();
            Toast.makeText(requireContext(), R.string.set_theme_success, Toast.LENGTH_LONG).show();
        });

        view.findViewById(R.id.theme1).setOnClickListener(v -> {
            Log.d("THEME", "onViewCreated: ");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("theme", 1);
            editor.apply();
            Toast.makeText(requireContext(), R.string.set_theme_success, Toast.LENGTH_LONG).show();
            Navigation.findNavController(view).popBackStack();
        });

        view.findViewById(R.id.theme2).setOnClickListener(v -> {
            Log.d("THEME", "onViewCreated: ");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("theme", 2);
            editor.apply();
            Toast.makeText(requireContext(), R.string.set_theme_success, Toast.LENGTH_LONG).show();
            Navigation.findNavController(view).popBackStack();
        });

        view.findViewById(R.id.back_button).setOnClickListener(v -> {
            Navigation.findNavController(view).popBackStack();
        });
    }

}