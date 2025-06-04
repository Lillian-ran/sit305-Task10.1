package com.example.personalizedlearningexperienceapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    private LinearLayout resultContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultContainer = findViewById(R.id.resultContainer);

        ArrayList<String> questions = getIntent().getStringArrayListExtra("questions");
        ArrayList<String> answers = getIntent().getStringArrayListExtra("answers");
        ArrayList<String> correctAnswers = getIntent().getStringArrayListExtra("correctAnswers");

        String optionsJson = getIntent().getStringExtra("optionsListJson");
        ArrayList<ArrayList<String>> optionsList = new ArrayList<>();

        try {
            JSONArray outer = new JSONArray(optionsJson);
            for (int i = 0; i < outer.length(); i++) {
                JSONArray inner = outer.getJSONArray(i);
                ArrayList<String> opt = new ArrayList<>();
                for (int j = 0; j < inner.length(); j++) {
                    opt.add(inner.getString(j));
                }
                optionsList.add(opt);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (questions != null && answers != null && correctAnswers != null && optionsList != null) {
            LayoutInflater inflater = LayoutInflater.from(this);

            for (int i = 0; i < questions.size(); i++) {
                View itemView = inflater.inflate(R.layout.item_history_question, resultContainer, false);

                TextView questionText = itemView.findViewById(R.id.question_text);
                TextView questionDesc = itemView.findViewById(R.id.question_desc);
                LinearLayout optionsContainer = itemView.findViewById(R.id.options_container);

                questionText.setText("Question " + (i + 1));
                questionDesc.setText(questions.get(i));

                ArrayList<String> options = optionsList.get(i);
                String userAns = answers.get(i);
                String correctAns = correctAnswers.get(i);

                for (int j = 0; j < options.size(); j++) {
                    String option = options.get(j);
                    TextView optionView = new TextView(this);
                    optionView.setText("• " + option);
                    optionView.setTextSize(14);
                    optionView.setPadding(8, 4, 8, 4);

                    // Get index of correct answer（A=0, B=1, C=2, D=3）
                    int correctIndex = correctAns.charAt(0) - 'A';
                    String correctOption = correctIndex >= 0 && correctIndex < options.size() ? options.get(correctIndex) : "";

                    if (option.equals(correctOption)) {
                        optionView.setTextColor(Color.GREEN);
                        optionView.setTypeface(null, Typeface.BOLD);
                        optionView.append("  √ Correct Answer");
                    } else if (option.equals(userAns) && !option.equals(correctOption)) {
                        optionView.setTextColor(Color.RED);
                        optionView.setTypeface(null, Typeface.BOLD);
                        optionView.append("  × Your Answer");
                    } else {
                        optionView.setTextColor(Color.WHITE);
                    }

                    optionsContainer.addView(optionView);
                }


                resultContainer.addView(itemView);
            }
        }

        Button btnAnotherQuiz = findViewById(R.id.btnAnotherQuiz);
        btnAnotherQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, QuestionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        Button btnExitToTask = findViewById(R.id.btnExitToTask);
        btnExitToTask.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, TaskActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

    }
}
