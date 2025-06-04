package com.example.personalizedlearningexperienceapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ShareBottomSheet extends BottomSheetDialogFragment {

    private EditText shareLinkEditText;
    private Button copyLinkButton;
    private TextView previewText;

    private QuizRecord latestRecord;

    private static final String BASE_URL = "http://192.168.1.6:5000";

    public ShareBottomSheet(QuizRecord latestRecord) {
        this.latestRecord = latestRecord;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_share, container, false);

        previewText = view.findViewById(R.id.previewText);
        shareLinkEditText = view.findViewById(R.id.shareLinkEditText);
        copyLinkButton = view.findViewById(R.id.copyLinkButton);

        if (latestRecord != null) {

            //answer preview
            String question = latestRecord.getQuestion();
            String correctLetter = latestRecord.getCorrectanswer(); // e.g., "C"
            String userAnswer = latestRecord.getUseranswer();
            String[] options = latestRecord.getOptions();
            String correctAnswer = "";

            try {
                int correctIndex = correctLetter.charAt(0) - 'A';
                if (correctIndex >= 0 && correctIndex < options.length) {
                    correctAnswer = options[correctIndex];
                }
            } catch (Exception e) {
                correctAnswer = "(Unknown)";
            }

            String preview = "My Quiz Result\n\n" +
                    "Q: " + question + "\n" +
                    "√ Correct: " + correctAnswer + "\n" +
                    "× Mine: " + userAnswer + "\n\n" +
                    "Share Link:";
            previewText.setText(preview);

            // generate link
            String shareUrl = BASE_URL + "/share-latest-result?username=" + latestRecord.getUsername();

            // show in edittext
            shareLinkEditText.setText(shareUrl);

            copyLinkButton.setOnClickListener(v -> {
                // copy to clipboard
                ClipboardManager clipboard = (ClipboardManager)
                        requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Share Link", shareUrl);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Link copied!", Toast.LENGTH_SHORT).show();

                // open share page
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareUrl);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
                Log.d("ShareBottomSheet", "username: " + latestRecord.getUsername());
                Log.d("ShareBottomSheet", "question: " + latestRecord.getQuestion());
                Log.d("ShareBottomSheet", "correct: " + latestRecord.getCorrectanswer());
                Log.d("ShareBottomSheet", "user: " + latestRecord.getUseranswer());

            });
        }

        return view;
    }
}