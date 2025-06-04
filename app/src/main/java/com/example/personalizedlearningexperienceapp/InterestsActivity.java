package com.example.personalizedlearningexperienceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

public class InterestsActivity extends AppCompatActivity {
    private Button nextButton;
    private CheckBox webDev, dataStructures, algorithm;

    private String username, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        // get user infor from set up page
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");

        // UI
        nextButton = findViewById(R.id.nextButton);
        webDev = findViewById(R.id.webDev);
        dataStructures = findViewById(R.id.dataStructures);
        algorithm = findViewById(R.id.algorithm);

        nextButton.setOnClickListener(v -> {
            ArrayList<String> interests = new ArrayList<>();
            if (webDev.isChecked()) interests.add("Web Development");
            if (dataStructures.isChecked()) interests.add("Data Structures");
            if (algorithm.isChecked()) interests.add("Algorithms");

            if (interests.isEmpty()) {
                Toast.makeText(this, "Please choose at least one interest", Toast.LENGTH_SHORT).show();
                return;
            }
            createUserAccount(username, email, password, interests);

            // back to login page
            Intent backToLogin = new Intent(InterestsActivity.this, MainActivity.class);
            startActivity(backToLogin);
            finish();
        });
    }

    private void createUserAccount(String username, String email, String password, ArrayList<String> interests) {
        Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
    }
}
