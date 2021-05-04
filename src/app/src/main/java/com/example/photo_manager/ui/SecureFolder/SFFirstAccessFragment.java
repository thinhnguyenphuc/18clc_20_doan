package com.example.photo_manager.ui.SecureFolder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.photo_manager.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.scottyab.aescrypt.AESCrypt;


public class SFFirstAccessFragment extends Fragment {

    SharedPreferences sharedPreferences;

    EditText password_input, repassword_input;
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
        return inflater.inflate(R.layout.fragment_s_f_first_access, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomNavigationView bnv = requireActivity().findViewById(R.id.nav_view);
        bnv.setVisibility(View.INVISIBLE);

        password_input = view.findViewById(R.id.password_input);
        repassword_input = view.findViewById(R.id.repassword_input);

        error_message = view.findViewById(R.id.error_message);

        view.findViewById(R.id.ok_button).setOnClickListener(v-> {
            String password = password_input.getText().toString();
            String repassword = repassword_input.getText().toString();
            if (password.isEmpty()) {
                error_message.setText(getString(R.string.empty_password_error));
            } else if (!password.equals(repassword)) {
                error_message.setText(getString(R.string.repassword_not_match_error));
            } else {
                try {
                    String encrypted_password = AESCrypt.encrypt(AESCryptPassword.value, password);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("password", encrypted_password);
                    editor.apply();
                    error_message.setText("");
                    NavDirections action = SFFirstAccessFragmentDirections.actionSFFirstAccessFragmentToSFPasswordFragment();
                    Navigation.findNavController(view).navigate(action);
                } catch (Exception e) {
                    e.printStackTrace();
                    error_message.setText(getString(R.string.save_password_error));
                }

            }

        });

    }
}