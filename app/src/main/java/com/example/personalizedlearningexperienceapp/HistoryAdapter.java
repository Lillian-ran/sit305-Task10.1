package com.example.personalizedlearningexperienceapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<HistoryQuestion> questions;
    private Context context;

    public HistoryAdapter(Context context, List<HistoryQuestion> questions) {
        this.context = context;
        this.questions = questions;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timestampTopic, questionText, questionDesc;
        LinearLayout optionsContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.question_text);
            questionDesc = itemView.findViewById(R.id.question_desc);
            optionsContainer = itemView.findViewById(R.id.options_container);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryQuestion q = questions.get(position);
        holder.questionText.setText((position + 1) + ". " + q.question);
        holder.questionDesc.setText("You can use any question here and setup a couple of answers.");

        holder.optionsContainer.removeAllViews();

        for (int i = 0; i < q.options.length; i++) {
            TextView optionView = new TextView(context);
            optionView.setText(q.options[i]);
            optionView.setTextSize(16);
            optionView.setPadding(10, 10, 10, 10);

            String label = q.options[i];

            if (i == q.correctAnswerIndex) {
                optionView.setTextColor(Color.GREEN);
                label += "  √ Correct Answer";
            }

            if (i == q.userAnswerIndex) {
                if (q.userAnswerIndex != q.correctAnswerIndex) {
                    optionView.setTextColor(Color.RED);
                    label += "  × Your Answer";
                }
            }

            optionView.setText(label);
            holder.optionsContainer.addView(optionView);
        }

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}

