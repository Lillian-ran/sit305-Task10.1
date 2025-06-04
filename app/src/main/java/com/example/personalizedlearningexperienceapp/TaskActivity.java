package com.example.personalizedlearningexperienceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class TaskActivity extends AppCompatActivity {

    private TextView usernameText;
    private Button enterTaskButton, enterProfileButton,enterUpgradeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        // get username
        String username = getIntent().getStringExtra("username");
        if (username == null) username = "User";

        usernameText = findViewById(R.id.usernameText);
        usernameText.setText(username);

        enterTaskButton = findViewById(R.id.enterTaskButton);
        enterProfileButton = findViewById(R.id.enterProfileButton);
        enterUpgradeButton = findViewById(R.id.enterUpgradeButton);

        // task page
        String finalUsername = username;
        enterTaskButton.setOnClickListener(v -> {
            Intent intent = new Intent(TaskActivity.this, QuestionActivity.class);
            intent.putExtra("username", finalUsername);
            startActivity(intent);
        });

        // profile page
        String finalUsername1 = username;
        enterProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(TaskActivity.this, ProfileActivity.class);
            intent.putExtra("username", finalUsername1);
            startActivity(intent);
        });

        // Upgrade page
        String finalUsername2 = username;
        enterUpgradeButton.setOnClickListener(v -> {
            Intent intent = new Intent(TaskActivity.this, UpgradeActivity.class);
            intent.putExtra("username", finalUsername2);
            startActivity(intent);
        });
    }
}

