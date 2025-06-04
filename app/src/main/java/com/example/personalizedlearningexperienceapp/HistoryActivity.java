package com.example.personalizedlearningexperienceapp;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedlearningexperienceapp.api.ApiClient;
import com.example.personalizedlearningexperienceapp.api.ApiService;

import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<HistoryQuestion> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<QuizRecord>> call = apiService.getQuizRecords();

        call.enqueue(new Callback<List<QuizRecord>>() {
            @Override
            public void onResponse(Call<List<QuizRecord>> call, Response<List<QuizRecord>> response) {
                if (response.isSuccessful()) {
                    questionList = new ArrayList<>();
                    for (QuizRecord record : response.body()) {
                        int userAnswerIndex = findIndex(record.getOptions(), record.getUseranswer());
                        int correctAnswerIndex = -1;
                        if (record.getCorrectanswer() != null && record.getCorrectanswer().length() == 1) {
                            correctAnswerIndex = record.getCorrectanswer().charAt(0) - 'A';
                        }

                        questionList.add(new HistoryQuestion(
                                record.getQuestion(),
                                record.getOptions(),
                                correctAnswerIndex,
                                userAnswerIndex
                        ));
                    }
                    adapter = new HistoryAdapter(HistoryActivity.this, questionList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<QuizRecord>> call, Throwable t) {
                Toast.makeText(HistoryActivity.this, "Error loading history", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int findIndex(String[] options, String answer) {
        if (options == null || answer == null) return -1;
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(answer)) return i;
        }
        return -1;
    }
}

//        // fake static data
//        questionList = new ArrayList<>();
//        questionList.add(new HistoryQuestion("What is the primary purpose of categorizing movies into genres?",
//                new String[]{"To encourage filmmakers to create more popular films.",
//                        "To help viewers understand the different types of stories being told.",
//                        "To make it easier for movie theaters to sell tickets.",
//                        "To simplify the process of film production."}, 1, 1)); // correct
//        questionList.add(new HistoryQuestion("Which of the following genres is characterized by complex plots and often involves multiple storylines?",
//                new String[]{"Romance",
//                        "Horror",
//                        "Science Fiction",
//                        "Western"}, 2, 3)); // incorrect
//        questionList.add(new HistoryQuestion("A film that primarily focuses on the emotions and experiences of its characters is most likely to be classified as which genre?",
//                new String[]{"Action",
//                        "Drama",
//                        "Comedy",
//                        "Thriller"}, 1, 1)); // correct
