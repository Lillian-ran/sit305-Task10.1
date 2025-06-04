package com.example.personalizedlearningexperienceapp.api;

import com.example.personalizedlearningexperienceapp.QuizRecord;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("/getQuizRecords?username=User")
    Call<List<QuizRecord>> getQuizRecords();
}

