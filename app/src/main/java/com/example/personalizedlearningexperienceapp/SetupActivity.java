package com.example.personalizedlearningexperienceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SetupActivity extends AppCompatActivity {
    EditText username, email, confirmEmail, password, confirmPassword, phonenumber;
    Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        // Initialize UI components
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        confirmEmail = findViewById(R.id.confirmEmail);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        phonenumber = findViewById(R.id.phonenumber);
        createAccountButton = findViewById(R.id.createAccountButton);

        createAccountButton.setOnClickListener(v -> {
            String user = username.getText().toString().trim();
            String userEmail = email.getText().toString().trim();
            String confirmEmailStr = confirmEmail.getText().toString().trim();
            String userPassword = password.getText().toString().trim();
            String confirmPasswordStr = confirmPassword.getText().toString().trim();
            String userPhone = phonenumber.getText().toString().trim();

            // Validate inputs
            if (user.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || userPhone.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!userEmail.equals(confirmEmailStr)) {
                Toast.makeText(this, "Emails do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!userPassword.equals(confirmPasswordStr)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Proceed with user registration
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();

            // Send user data to InterestsActivity
            Intent intent = new Intent(this, InterestsActivity.class);
            intent.putExtra("username", user);
            intent.putExtra("email", userEmail);
            intent.putExtra("password", userPassword);
            startActivity(intent);

            createUserAccount(user, userEmail, userPassword, userPhone);
        });
    }

    private void createUserAccount(String username, String email, String password, String phone) {
        // Optional: use API to store user data
        // This is a placeholder for backend logic
    }
}

