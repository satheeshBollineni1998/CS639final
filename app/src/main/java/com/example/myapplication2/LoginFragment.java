package com.example.myapplication2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonAction;
    private TextView textViewActionPrompt;
    private FirebaseAuth firebaseAuth;
    private boolean isRegisterMode = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonAction = view.findViewById(R.id.buttonLogin);
        textViewActionPrompt = view.findViewById(R.id.textViewRegister);

        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRegisterMode) {
                    registerUser();
                } else {
                    loginUser();
                }
            }
        });

        textViewActionPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMode();
            }
        });

        return view;
    }

    private void toggleMode() {
        isRegisterMode = !isRegisterMode;
        if (isRegisterMode) {
            // Switch to registration mode
            textViewActionPrompt.setText(R.string.prompt_login);
            buttonAction.setText(R.string.action_register);
        } else {
            // Switch to login mode
            textViewActionPrompt.setText(R.string.prompt_register);
            buttonAction.setText(R.string.action_login);
        }
        // Clear input fields
        editTextEmail.setText("");
        editTextPassword.setText("");
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Navigate to the first fragment after successful login
                        navigateToFirstFragment();
                    } else {
                        handleAuthError(task.getException());
                    }
                });
    }


    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Toast.makeText(requireContext(), "Registration successful: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    } else {
                        handleAuthError(task.getException());
                    }
                });
    }

    private void handleAuthError(Exception exception) {
        if (exception instanceof FirebaseAuthUserCollisionException) {
            Toast.makeText(requireContext(), "Email already in use", Toast.LENGTH_SHORT).show();
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
        }
    }
    private void navigateToFirstFragment() {
        // Navigate to the first fragment using Navigation component
        NavHostFragment.findNavController(this).navigate(R.id.action_LoginFragment_to_FirstFragment);
    }
}
