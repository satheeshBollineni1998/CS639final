package com.example.myapplication2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class LoginFragment extends Fragment {


    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_login, container, false);

        // Initialize Views
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonLogin = view.findViewById(R.id.buttonLogin);

        // Set OnClickListener for the login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input from the EditText fields
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Validate the input (for example, check if the fields are not empty)
                if (!email.isEmpty() && !password.isEmpty()) {
                    // You can add your login logic here
                    // For simplicity, let's just display a toast message indicating successful login
                    Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    navigateToFirstFragment();
                } else {
                    Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
    private void navigateToFirstFragment() {
        // Navigate to the existing FirstFragment using NavHostFragment
        NavHostFragment.findNavController(this).navigate(R.id.action_LoginFragment_to_FirstFragment);
    }
}
