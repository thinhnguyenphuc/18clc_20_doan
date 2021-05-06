package com.example.photo_manager.ui.SecureFolder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photo_manager.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.scottyab.aescrypt.AESCrypt;

public class SFPasswordFragment extends Fragment {

    SharedPreferences sharedPreferences;
    NavController navController;

    EditText password_input;
    TextView error_message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireActivity()
                .getSharedPreferences("com.example.photo_manager.ui.SecurityFolder", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_s_f_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        BottomNavigationView bnv = requireActivity().findViewById(R.id.nav_view);
        bnv.setVisibility(View.INVISIBLE);

        String password = sharedPreferences.getString("password", "");

        if (password.isEmpty()) {
            navController.popBackStack();
        }

        password_input = view.findViewById(R.id.password_input);
        error_message = view.findViewById(R.id.error_message);

        try {
            String decrypted_password = AESCrypt.decrypt(AESCryptPassword.value, password);


            view.findViewById(R.id.ok_button).setOnClickListener(v -> {
                String input_password = password_input.getText().toString();
                if (input_password.isEmpty()) {
                    error_message.setText(getString(R.string.empty_password_error));
                } else if (!input_password.equals(decrypted_password)) {
                    error_message.setText(getString(R.string.incorrect_password));
                } else {
                    error_message.setText("");
                    NavDirections action = SFPasswordFragmentDirections.actionSFPasswordFragmentToSecurityFolderFragment();
                    navController.navigate(action);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), R.string.decrypt_password_error, Toast.LENGTH_LONG).show();
            navController.popBackStack();
        }

        view.findViewById(R.id.back_button).setOnClickListener(v -> {
            navController.popBackStack();
        });

    }
}