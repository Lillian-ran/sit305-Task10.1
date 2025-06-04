package com.example.personalizedlearningexperienceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personalizedlearningexperienceapp.api.ApiClient;
import com.example.personalizedlearningexperienceapp.api.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        LinearLayout cardTotal = findViewById(R.id.card_total);
        LinearLayout cardCorrect = findViewById(R.id.card_correct);
        LinearLayout cardIncorrect = findViewById(R.id.card_incorrect);

        cardTotal.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
            intent.putExtra("type", "total");
            startActivity(intent);
        });

        cardCorrect.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
            intent.putExtra("type", "correct");
            startActivity(intent);
        });

        cardIncorrect.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HistoryActivity.class);
            intent.putExtra("type", "incorrect");
            startActivity(intent);
        });


        Button shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(v -> {
            // get records
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<List<QuizRecord>> call = apiService.getQuizRecords();

            call.enqueue(new Callback<List<QuizRecord>>() {
                @Override
                public void onResponse(Call<List<QuizRecord>> call, Response<List<QuizRecord>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        List<QuizRecord> allRecords = response.body();
                        QuizRecord latest = allRecords.get(allRecords.size() - 1);

                        String username = getIntent().getStringExtra("username");
                        latest.setUsername(username);

                        ShareBottomSheet sheet = new ShareBottomSheet(latest);
                        sheet.show(getSupportFragmentManager(), "ShareBottomSheet");
                    } else {
                        Toast.makeText(ProfileActivity.this, "No quiz record available.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<QuizRecord>> call, Throwable t) {
                    Toast.makeText(ProfileActivity.this, "Failed to fetch records", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
