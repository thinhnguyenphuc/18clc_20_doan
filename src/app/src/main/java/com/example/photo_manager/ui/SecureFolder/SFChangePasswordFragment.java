package com.example.photo_manager.ui.SecureFolder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.photo_manager.R;
import com.scottyab.aescrypt.AESCrypt;


public class SFChangePasswordFragment extends Fragment {

    private EditText oldpass, newpass, renewpass;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String password, oldPassword, newPassword, renewPassword;

    private TextView old_error_message, new_error_message;

    private NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_s_f_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        assignVariables(view);

        if (password.isEmpty()) {
            old_error_message.setText(getString(R.string.old_password_retrieve_error));
        }

        int count = 0;
        int maxTries = 10;

        try {
            password = AESCrypt.decrypt(AESCryptPassword.value, password);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), R.string.decrypt_password_error, Toast.LENGTH_LONG).show();
            navController.popBackStack();
        }

        view.findViewById(R.id.ok_button).setOnClickListener(v -> {
            boolean flag_old = false, flag_new = false;
            oldPassword = oldpass.getText().toString();
            newPassword = newpass.getText().toString();
            renewPassword = renewpass.getText().toString();

            if (oldPassword.equals(password)) {
                flag_old = true;
            } else {
                old_error_message.setText(getString(R.string.wrong_old_password));
            }

            if (newPassword.equals(renewPassword)) {
                flag_new = true;
            } else {
                new_error_message.setText(getString(R.string.wrong_renew_password));
            }

            if (flag_old && flag_new) {
                new_error_message.setText("");
                old_error_message.setText("");
                if (saveNewPassword(newPassword)) {
                    Toast.makeText(requireContext(), R.string.save_new_password_success, Toast.LENGTH_LONG).show();
                    navController.popBackStack();
                } else {
                    Toast.makeText(requireContext(), R.string.save_new_password_fail, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void assignVariables(View view) {
        sharedPreferences = requireActivity()
                .getSharedPreferences("com.example.photo_manager.ui.SecurityFolder", Context.MODE_PRIVATE);

        password = sharedPreferences.getString("password", "");

        oldpass = view.findViewById(R.id.old_password_input);
        newpass = view.findViewById(R.id.new_password_input);
        renewpass = view.findViewById(R.id.re_new_password_input);

        old_error_message = view.findViewById(R.id.error_message_old);
        new_error_message = view.findViewById(R.id.error_message_new);
    }

    private boolean saveNewPassword(String password) {
        try {
            editor = sharedPreferences.edit();
            editor.putString("password", AESCrypt.encrypt(AESCryptPassword.value, password));
            editor.apply();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}